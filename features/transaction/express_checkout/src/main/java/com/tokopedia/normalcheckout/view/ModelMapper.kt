package com.tokopedia.normalcheckout.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.constant.MAX_QUANTITY
import com.tokopedia.expresscheckout.data.entity.response.atc.Message
import com.tokopedia.expresscheckout.domain.model.atc.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import kotlin.math.roundToInt

object ModelMapper {

    fun convertToModels(productInfo: ProductInfo,
                        productVariant: ProductVariant?,
                        noteString: String?, quantity: Int = 0): ArrayList<Visitable<*>> {
        val dataList: ArrayList<Visitable<*>> = ArrayList()
        dataList.add(convertToProductViewModel(productInfo))
        dataList.add(convertToQuantityViewModel(productInfo, quantity))
        if (productVariant != null && productVariant.hasChildren) {

        }
        productVariant?.run {
            val variantViewModelList = ArrayList<TypeVariantViewModel>()
            if (variantViewModelList.isNotEmpty()) {
                dataList.addAll(variantViewModelList)
            }
        }
        dataList.add(convertToNoteViewModel(noteString))
        return dataList
    }

    fun convertToNoteViewModel(noteString: String?): NoteViewModel {
        val noteViewModel = NoteViewModel()
        noteViewModel.noteCharMax = 144
        if (noteString != null && noteString.isNotEmpty()) {
            noteViewModel.note = noteString
        }
        return noteViewModel
    }

    fun convertToProductViewModel(productInfo: ProductInfo): ProductViewModel {
        val productViewModel = ProductViewModel()
        productViewModel.parentId = productInfo.parentProductId.toInt()
        productViewModel.productImageUrl = productInfo.firstThumbnailPicture
        productViewModel.productName = productInfo.basic.name
        productViewModel.minOrderQuantity = productInfo.basic.minOrder
        productViewModel.maxOrderQuantity = when {
            productInfo.stock.useStock && productInfo.stock.value > 0 ->
                productInfo.stock.value
            productInfo.basic.maxOrder > 0 -> productInfo.basic.maxOrder
            else -> MAX_QUANTITY
        }
        productViewModel.productPrice = if (productInfo.hasActiveCampaign && productInfo.campaign.discountedPrice > 0) {
            productInfo.campaign.discountedPrice.roundToInt()
        } else {
            productInfo.basic.price.roundToInt()
        }
        productViewModel.originalPrice = if (productInfo.hasActiveCampaign && productInfo.campaign.originalPrice > 0) {
            productInfo.campaign.originalPrice.roundToInt()
        } else {
            productInfo.basic.price.roundToInt()
        }
        productViewModel.discountedPercentage = if (productInfo.hasActiveCampaign && productInfo.campaign.percentage > 0) {
            productInfo.campaign.percentage
        } else {
            0f
        }
        return productViewModel
    }

    private fun checkChildAvailable(productChild: ProductChild,
                                    optionViewModelId: Int,
                                    currentChangedOptionId: Int,
                                    otherVariantSelectedOptionIds: ArrayList<Int>): Boolean {

        // Check is child with newly selected option id, other variant selected option ids,
        // and current looping variant option id is available
        var otherSelectedOptionIdCount = 0
        for (optionId: Int in otherVariantSelectedOptionIds) {
            if (optionId in productChild.optionsId) {
                otherSelectedOptionIdCount++
            }
        }

        val otherSelectedOptionIdCountEqual = otherSelectedOptionIdCount == otherVariantSelectedOptionIds.size
        val currentChangedOptionIdAvailable = currentChangedOptionId in productChild.optionsId
        val optionViewModelIdAvailable = optionViewModelId in productChild.optionsId

        return productChild.isAvailable && currentChangedOptionIdAvailable && optionViewModelIdAvailable && otherSelectedOptionIdCountEqual
    }

    fun convertToQuantityViewModel(atcResponseModel: AtcResponseModel,
                                   productViewModel: ProductViewModel): QuantityViewModel {
        val quantityViewModel = QuantityViewModel()
        val messagesModel = atcResponseModel.atcDataModel?.messagesModel
        quantityViewModel.errorFieldBetween = messagesModel?.get(Message.ERROR_FIELD_BETWEEN) ?: ""
        quantityViewModel.errorFieldMaxChar = messagesModel?.get(Message.ERROR_FIELD_MAX_CHAR) ?: ""
        quantityViewModel.errorFieldRequired = messagesModel?.get(Message.ERROR_FIELD_REQUIRED)
                ?: ""
        quantityViewModel.errorProductAvailableStock = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK)
                ?: ""
        quantityViewModel.errorProductAvailableStockDetail = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL)
                ?: ""
        quantityViewModel.errorProductMaxQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MAX_QUANTITY)
                ?: ""
        quantityViewModel.errorProductMinQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MIN_QUANTITY)
                ?: ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = productViewModel.maxOrderQuantity
        quantityViewModel.minOrderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.orderQuantity = productViewModel.minOrderQuantity
        quantityViewModel.stockWording = ""

        return quantityViewModel
    }

    /**
     * convert the product model got from api and the selected quantity to the view model to be rendered to view
     * The View model has the upper and lower bound to be set.
     * We should check the campaign (because the campaign has its own upper and lower stock)
     * if it is not in campaign, use the default stock
     * If it has stock, the upper max will be that stock, otherwise, use the max order value
     */
    fun convertToQuantityViewModel(productInfo: ProductInfo, quantity: Int = 0): QuantityViewModel {
        val quantityViewModel = QuantityViewModel()
        quantityViewModel.errorProductMaxQuantity = ""
        quantityViewModel.errorProductMinQuantity = ""
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity =
                when {
                    productInfo.stock.useStock && productInfo.stock.value > 0 ->
                        productInfo.stock.value
                    productInfo.basic.maxOrder > 0 -> productInfo.basic.maxOrder
                    else -> MAX_QUANTITY
                }
        quantityViewModel.minOrderQuantity = productInfo.basic.minOrder
        quantityViewModel.orderQuantity = if (quantity > 0) quantity else productInfo.basic.minOrder
        quantityViewModel.stockWording = productInfo.stock.stockWording

        return quantityViewModel
    }

    fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel {
        val typeVariantViewModel = TypeVariantViewModel(null)

        val optionVariantViewModels = ArrayList<OptionVariantViewModel>()
        val optionModels = variantModel.optionModels
        if (optionModels != null) {
            for (optionModel: OptionModel in optionModels) {
                optionVariantViewModels.add(
                        convertToOptionVariantViewModel(optionModel, variantModel.productVariantId
                                ?: 0, childrenModel)
                )
            }
        }
        typeVariantViewModel.variantId = variantModel.productVariantId ?: 0
        typeVariantViewModel.variantOptions = optionVariantViewModels
        typeVariantViewModel.variantName = variantModel.variantName ?: ""

        return typeVariantViewModel
    }

    fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel {
        val optionVariantViewModel = OptionVariantViewModel(null)
        optionVariantViewModel.variantId = variantId
        optionVariantViewModel.optionId = optionModel.id
        optionVariantViewModel.variantHex = optionModel.hex ?: ""
        optionVariantViewModel.variantName = optionModel.value ?: ""

        var hasAvailableChild = false
        for (childModel: ChildModel in childrenModel) {
            if (childModel.isBuyable == true && childModel.optionIds?.contains(optionVariantViewModel.optionId) == true) {
                hasAvailableChild = true
                break
            }
        }
        optionVariantViewModel.hasAvailableChild = hasAvailableChild
        if (!hasAvailableChild) {
            optionVariantViewModel.currentState = optionVariantViewModel.STATE_NOT_AVAILABLE
        }

        return optionVariantViewModel
    }

    private fun validateVariantCombination(productVariantDataModel: ProductVariantDataModel): Boolean {
        val variantModels = productVariantDataModel.variantModels
        if (variantModels != null) {
            val variantOptionSizeList: ArrayList<Int> = ArrayList()
            for (variantModel: VariantModel in variantModels) {
                val optionModels = variantModel.optionModels
                variantOptionSizeList.add(optionModels?.size ?: 0)
            }

            var variantCombinationSize = 1
            for (optionSize: Int in variantOptionSizeList) {
                variantCombinationSize *= optionSize
            }

            return variantCombinationSize == productVariantDataModel.childModels?.size
        }

        return false
    }

    private fun validateVariantChildren(productVariantDataModel: ProductVariantDataModel): Boolean {

        val childModels = productVariantDataModel.childModels
        if (childModels != null) {
            for (childModel: ChildModel in childModels) {
                if (childModel.optionIds?.size != productVariantDataModel.variantModels?.size) {
                    return false
                }
            }

            var hasValidVariant = false
            for (childModel: ChildModel in childModels) {
                if (childModel.isBuyable == true) {
                    hasValidVariant = true
                    break
                }
            }

            return hasValidVariant
        }
        return false
    }

}