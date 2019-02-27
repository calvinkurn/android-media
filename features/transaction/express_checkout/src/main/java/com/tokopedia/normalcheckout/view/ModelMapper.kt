package com.tokopedia.normalcheckout.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.constant.MAX_QUANTITY
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED
import com.tokopedia.product.detail.common.data.model.Picture
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.ACTIVE
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.WAREHOUSE
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.Option
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import kotlin.math.roundToInt

object ModelMapper {

    fun convertToModels(productInfo: ProductInfo,
                        productVariant: ProductVariant?,
                        noteString: String?, quantity: Int = 0): ArrayList<Visitable<*>> {
        val dataList: ArrayList<Visitable<*>> = ArrayList()
        dataList.add(convertToProductViewModel(productInfo))
        if (productVariant != null && productVariant.hasChildren) {
            val variantModelList = convertToProductVariantViewModel(productVariant, productInfo)
            if (variantModelList != null && variantModelList.isNotEmpty()) {
                dataList.addAll(variantModelList)
            }
        }
        productVariant?.run {
            val variantViewModelList = ArrayList<TypeVariantViewModel>()
            if (variantViewModelList.isNotEmpty()) {
                dataList.addAll(variantViewModelList)
            }
        }
        dataList.add(convertToQuantityViewModel(productInfo, quantity))
        dataList.add(convertToNoteViewModel(noteString))
        return dataList
    }

    fun convertToModels(originalProductInfo: ProductInfo,
                        selectedVariant: Child?): ProductInfo {
        return originalProductInfo.copy(
                basic = originalProductInfo.basic.copy(
                        id = selectedVariant?.productId ?: 0,
                        price = selectedVariant?.price ?: 0f,
                        name = selectedVariant?.name ?: originalProductInfo.basic.name,
                        minOrder = selectedVariant?.stock?.minimumOrder ?: 0,
                        maxOrder = selectedVariant?.stock?.maximumOrder ?: 0,
                        status = if (selectedVariant?.isBuyable == true) {
                            ACTIVE
                        } else {
                            WAREHOUSE
                        },
                        sku = selectedVariant?.sku ?: "",
                        isEligibleCod = selectedVariant?.isCod ?: false
                ),
                pictures = if (selectedVariant?.hasPicture == true) {
                    mutableListOf(
                            Picture(urlOriginal =
                            selectedVariant.picture?.original ?: "",
                                    urlThumbnail = selectedVariant.picture?.thumbnail ?: "",
                                    url300 = selectedVariant.picture?.thumbnail ?: ""))
                } else {
                    originalProductInfo.pictures
                },
                campaign = if (selectedVariant?.campaign?.activeAndHasId == true) {
                    originalProductInfo.campaign.copy(
                            id = selectedVariant.campaign?.campaignID ?: "",
                            isActive = selectedVariant.campaign?.isActive ?: false,
                            originalPrice = selectedVariant.campaign?.originalPrice ?: 0f,
                            discountedPrice = selectedVariant.campaign?.discountedPrice ?: 0f,
                            percentage = selectedVariant.campaign?.discountedPercentage ?: 0f,
                            startDate = selectedVariant.campaign?.startDate ?: "",
                            endDate = selectedVariant.campaign?.endDate ?: ""
                    )
                } else {
                    originalProductInfo.campaign
                },
                stock = originalProductInfo.stock.copy(
                        useStock = selectedVariant?.stock?.alwaysAvailable == true ||
                                (selectedVariant?.stock?.isLimitedStock == true && (selectedVariant.stock?.stock
                                        ?: 0) > 0),
                        value = selectedVariant?.stock?.stock ?: 0,
                        stockWording = selectedVariant?.stock?.stockWordingHTML ?: ""
                )
        )
    }

    /**
     * convert the user input notestring to viewmodel
     */
    fun convertToNoteViewModel(noteString: String?): NoteViewModel {
        val noteViewModel = NoteViewModel()
        noteViewModel.noteCharMax = 144
        if (noteString != null && noteString.isNotEmpty()) {
            noteViewModel.note = noteString
        }
        return noteViewModel
    }

    /**
     * convert the product Info to ProductViewModel
     * to show the basic data of the product: name, image, price, discount
     */
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

    /**
     * convert product variant (Color, size) with options to list of view model
     */
    fun convertToProductVariantViewModel(productVariant: ProductVariant,
                                         selectedProduct: ProductInfo): List<TypeVariantViewModel>? {
        val variantViewModelList = ArrayList<TypeVariantViewModel>()
        val variantChildrenValidation = validateVariantChildren(productVariant.children, productVariant.variant?.size
                ?: 0)
        if (!variantChildrenValidation) {
            return null
        }
        val variantModels = productVariant.variant
        var level = 0
        for (variantModel: Variant in variantModels) {
            val typeVariantViewModel = convertToTypeVariantViewModel(productVariant, variantModel, selectedProduct, level)
                    ?: continue
            level++
            variantViewModelList.add(typeVariantViewModel)
        }
        return variantViewModelList
    }

    /**
     * convert the product model got from api and the selected quantity to the view model to be rendered to view
     * The View model has the upper and lower bound to be set.
     * We should check the campaign (because the campaign has its own upper and lower stock)
     * always use the stock outside the campaign, so the stock at the campaign is ignored.
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

    /**
     * Convert to the same variant (for example color)
     * Convert variant red + child({red,S}, {red,M}, blue{s}, blue{M}) to {red,S}, {red,M}
     */
    fun convertToTypeVariantViewModel(productVariant: ProductVariant, variantModel: Variant,
                                      selectedProduct: ProductInfo,
                                      variantLevel: Int): TypeVariantViewModel? {
        val typeVariantViewModel = TypeVariantViewModel()
        val childrenModel = productVariant.children

        val optionVariantViewModels = ArrayList<OptionVariantViewModel>()
        val optionModels = variantModel.options
        if (optionModels != null) {
            val selectedOptionsIds = getOptionsIds(childrenModel, selectedProduct)
            for (optionModel: Option in optionModels) {
                optionVariantViewModels.add(
                        convertToOptionVariantViewModel(optionModel, variantModel.v ?: 0,
                                childrenModel, selectedOptionsIds, variantLevel)
                )
            }
        }
        typeVariantViewModel.variantId = variantModel.v ?: 0
        typeVariantViewModel.variantOptions = optionVariantViewModels
        typeVariantViewModel.variantName = variantModel.name ?: ""
        typeVariantViewModel.variantGuideline =
                if (variantModel.isSizeIdentifier && productVariant.sizeChart.isNotEmpty()) {
                    productVariant.sizeChart
                } else {
                    ""
                }
        return typeVariantViewModel
    }

    private fun getOptionsIds(childrenModel: List<Child>, selectedProduct: ProductInfo): List<Int> {
        for (childModel: Child in childrenModel) {
            if (selectedProduct.basic.id.toString().equals(childModel.productId.toString(), false)) {
                return childModel.optionIds
            }
        }
        return listOf()
    }

    /**
     * convert {red}, {M} to view model, but also determine the state STATE_SELECTED, STATE_NOT_AVAILABLE, or STATE_SELECTED
     * SELECTED if options is in selectedOptionsIds
     * STATE_NOT_AVAILABLE if all the children is not buyable.
     */
    fun convertToOptionVariantViewModel(optionModel: Option,
                                        variantId: Int,
                                        childrenModel: List<Child>,
                                        selectedOptionsIds: List<Int>,
                                        variantLevel: Int): OptionVariantViewModel {
        val optionVariantViewModel = OptionVariantViewModel()
        optionVariantViewModel.variantId = variantId
        optionVariantViewModel.optionId = optionModel.id ?: 0
        optionVariantViewModel.variantHex = optionModel.hex ?: ""
        optionVariantViewModel.variantName = optionModel.value ?: ""

        val partialSelectedListByLevel = if (selectedOptionsIds.isNotEmpty()) {
            selectedOptionsIds.subList(0, variantLevel)
        } else {
            selectedOptionsIds
        }

        optionVariantViewModel.currentState = STATE_NOT_AVAILABLE

        if (selectedOptionsIds.isNotEmpty() && optionModel.id in selectedOptionsIds) {
            optionVariantViewModel.currentState = STATE_SELECTED
        } else {
            for (childModel: Child in childrenModel) {
                if (childModel.isBuyable && childModel.optionIds.contains(optionVariantViewModel.optionId)) {
                    if (partialSelectedListByLevel.isEmpty()) {
                        // no need to check more. This will be enabled
                        optionVariantViewModel.currentState = STATE_NOT_SELECTED
                    } else {
                        val childOptionId = childModel.optionIds.getOrNull(variantLevel)
                        if (childOptionId != null) {
                            var selectedCounter = 0
                            for (selectedOptionId: Int in partialSelectedListByLevel) {
                                if (childModel.optionIds.contains(selectedOptionId)) {
                                    selectedCounter++
                                }
                            }
                            if (selectedCounter == variantLevel) {
                                optionVariantViewModel.currentState = STATE_NOT_SELECTED
                            }
                        }
                    }

                }
            }
        }
        return optionVariantViewModel
    }

    /**
     * Example validation:
     * each child should have the same size option with the variant size.
     * Example: option[red,XL] has size 2, should same with the variant size (color and size)
     */
    private fun validateVariantChildren(childList: List<Child>, variantSize: Int): Boolean {
        for (childModel: Child in childList) {
            if (childModel.optionIds.size != variantSize) {
                return false
            }
        }
        return true
    }

}