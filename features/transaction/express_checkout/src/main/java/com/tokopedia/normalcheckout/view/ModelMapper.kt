package com.tokopedia.normalcheckout.view

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.constant.MAX_QUANTITY
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED
import com.tokopedia.product.detail.common.data.model.product.Picture
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.ACTIVE
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.WAREHOUSE
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.Option
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import kotlin.math.roundToInt

object ModelMapper {

    fun convertVariantToModels(productInfo: ProductInfo,
                               multiorigin: MultiOriginWarehouse?,
                               productVariant: ProductVariant?,
                               noteString: String?, quantity: Int = 0): ArrayList<Visitable<*>> {
        val dataList: ArrayList<Visitable<*>> = ArrayList()
        dataList.add(convertToProductViewModel(productInfo, multiorigin))
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

    fun convertVariantToModels(originalProductInfo: ProductInfo,
                               selectedVariant: Child?,
                               variantRef: List<Variant>? = null): ProductInfo {
        return originalProductInfo.copy(
            basic = originalProductInfo.basic.copy(
                id = selectedVariant?.productId ?: 0,
                price = selectedVariant?.price ?: 0f,
                name = if (selectedVariant == null) {
                    originalProductInfo.basic.name
                } else {
                    selectedVariant.name
                },
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
                val list = mutableListOf<Picture>()
                val variantPicture = Picture(urlOriginal =
                selectedVariant.picture?.original ?: "",
                        urlThumbnail = selectedVariant.picture?.thumbnail ?: "",
                        url300 = selectedVariant.picture?.thumbnail ?: "")
                list.add(variantPicture)
                list
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
            ),
            variant = originalProductInfo.variant.copy(
                isVariant = true,
                parentID = originalProductInfo.parentProductId
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
    fun convertToProductViewModel(productInfo: ProductInfo, multiorigin: MultiOriginWarehouse? = null): ProductViewModel {
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
        productViewModel.productPrice = if (multiorigin != null && multiorigin.warehouseInfo.id.isNotBlank()){
            multiorigin.price
        } else if (productInfo.hasActiveCampaign && productInfo.campaign.discountedPrice > 0) {
            productInfo.campaign.discountedPrice.roundToInt()
        } else {
            productInfo.basic.price.roundToInt()
        }

        productViewModel.originalPrice = if (multiorigin != null && multiorigin.warehouseInfo.id.isNotBlank()){
            productInfo.basic.price.roundToInt()
        } else if (productInfo.hasActiveCampaign && productInfo.campaign.originalPrice > 0) {
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
        val variantChildrenValidation = validateVariantChildren(productVariant.children, productVariant.variant.size)
        if (!variantChildrenValidation) {
            return null
        }
        val variantModels = productVariant.variant
        var level = 0
        for (variantModel: Variant in variantModels) {
            val typeVariantViewModel =
                convertToTypeVariantViewModel(productVariant, variantModel, selectedProduct, level,
                    (level + 1) == variantModels.size)
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
        quantityViewModel.minOrderQuantity = if (productInfo.basic.minOrder > 0) productInfo.basic.minOrder else 1
        quantityViewModel.orderQuantity = if (quantity > 0) quantity else quantityViewModel.minOrderQuantity
        quantityViewModel.stockWording = productInfo.stock.stockWording

        return quantityViewModel
    }

    /**
     * Convert to the same variant (for example color)
     * Convert variant red + child({red,S}, {red,M}, blue{s}, blue{M}) to {red,S}, {red,M}
     */
    fun convertToTypeVariantViewModel(productVariant: ProductVariant, variantModel: Variant,
                                      selectedProduct: ProductInfo,
                                      variantLevel: Int,
                                      isLeaf: Boolean): TypeVariantViewModel? {
        val typeVariantViewModel = TypeVariantViewModel()
        val childrenModel = productVariant.children

        val optionVariantViewModels = ArrayList<OptionVariantViewModel>()
        val optionModels = variantModel.options
        val selectedOptionsIds = getOptionsIds(childrenModel, selectedProduct)
        val isSelectedProductBuyable = selectedProduct.isBuyable
        for (optionModel: Option in optionModels) {
            optionVariantViewModels.add(
                convertToOptionVariantViewModel(optionModel, variantModel.v ?: 0,
                    childrenModel, selectedOptionsIds, isSelectedProductBuyable, variantLevel, isLeaf)
            )
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
        typeVariantViewModel.variantIdentifier = variantModel.identifier ?: ""
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
                                        isSelectedProductBuyable: Boolean,
                                        variantLevel: Int,
                                        isLeaf: Boolean): OptionVariantViewModel {
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
            // if the option is in selectedOptionList, means the option is selected.
            // check if the product buyable, then it is selected
            // else need to check if there is child is buyable to be selected.
            if (isSelectedProductBuyable) {
                optionVariantViewModel.currentState = STATE_SELECTED
            } else if (!isLeaf) {
                for (childModel: Child in childrenModel) {
                    if (childModel.isBuyable &&
                        childModel.optionIds.get(variantLevel) == optionVariantViewModel.optionId) {
                        optionVariantViewModel.currentState = STATE_SELECTED
                        break
                    }
                }
            }
        } else {
            for (childModel: Child in childrenModel) {
                if (childModel.isBuyable &&
                    childModel.optionIds.get(variantLevel) == optionVariantViewModel.optionId) {
                    if (partialSelectedListByLevel.isEmpty()) {
                        // no need to check more. This will be enabled, since it is the first level
                        optionVariantViewModel.currentState = STATE_NOT_SELECTED
                    } else {
                        // check if the child option match with the selected options
                        // example, we have option: [100, 200, 300]
                        // selection option is [100,200,301]
                        // if this in level 2, we just need to check [100, 200] (first 2 levels)
                        val childOptionId = childModel.optionIds.getOrNull(variantLevel)
                        if (childOptionId != null) {
                            if (childModel.optionIds.subList(0, variantLevel) == partialSelectedListByLevel) {
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