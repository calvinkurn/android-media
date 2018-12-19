package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutFormData
import com.tokopedia.expresscheckout.data.entity.UserProfile
import com.tokopedia.transactiondata.entity.response.variantdata.Child
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.ProductVariantData
import com.tokopedia.transactiondata.entity.response.variantdata.Variant
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.transactiondata.entity.response.shippingaddressform.Product

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ViewModelMapper : DataMapper {

    override fun convertToViewModels(expressCheckoutFormData: ExpressCheckoutFormData): ArrayList<Visitable<*>> {
        var dataList: ArrayList<Visitable<*>> = ArrayList()

        var variantViewModelList = ArrayList<TypeVariantViewModel>()
        if (expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData != null &&
                expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData.isNotEmpty()) {
            val variantCombinationValidation = validateVariantCombination(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
            val variantChildrenValidation = validateVariantChildren(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
            var hasVariant = variantCombinationValidation && variantChildrenValidation
            var children = expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].children
            if (hasVariant) {
                for (variant: Variant in expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].variants) {
                    variantViewModelList.add(convertToTypeVariantViewModel(variant, children))
                }
            }
        }

        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToProfileViewModel(expressCheckoutFormData))
        }
        var checkoutVariantProductViewModel = convertToProductViewModel(expressCheckoutFormData, variantViewModelList)
        dataList.add(checkoutVariantProductViewModel)
        dataList.add(convertToQuantityViewModel(expressCheckoutFormData, checkoutVariantProductViewModel))
        if (variantViewModelList.isNotEmpty()) {
            dataList.addAll(variantViewModelList)
        }
        dataList.add(convertToNoteViewModel(expressCheckoutFormData))
        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToSummaryViewModel(expressCheckoutFormData))
        }

        return dataList
    }

    override fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): NoteViewModel {
        var checkoutVariantNoteViewModel = NoteViewModel()
        checkoutVariantNoteViewModel.noteCharMax = expressCheckoutFormData.maxCharNote ?: 144
        checkoutVariantNoteViewModel.note = ""

        return checkoutVariantNoteViewModel
    }

    override fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData,
                                           typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel {
        val product: Product = expressCheckoutFormData.cart.groupShops[0].products[0]
        var checkoutVariantProductViewModel = ProductViewModel()
        checkoutVariantProductViewModel.productImageUrl = product.productImageSrc200Square
        checkoutVariantProductViewModel.productName = product.productName
        checkoutVariantProductViewModel.minOrderQuantity = product.productMinOrder
        if (product.productInvenageValue > 0) {
            checkoutVariantProductViewModel.maxOrderQuantity = product.productInvenageValue
        } else {
            checkoutVariantProductViewModel.maxOrderQuantity = expressCheckoutFormData.maxQuantity ?: 10000
        }
        checkoutVariantProductViewModel.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.productPrice, false)
        var productChildList = ArrayList<ProductChild>()
        var hasSelectedDefaultVariant = false
        if (typeVariantViewModels.size > 0) {
            for (child: Child in expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].children) {
                var productChild = ProductChild()
                productChild.productId = child.productId
                productChild.productName = child.name
                productChild.isAvailable = child.isBuyable
                productChild.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(child.price, false)
                productChild.stockWording = child.stockWording
                productChild.minOrder = child.minOrder
                productChild.maxOrder = child.maxOrder
                productChild.optionsId = child.optionIds
                if (productChild.isAvailable && !hasSelectedDefaultVariant) {
                    productChild.isSelected = true
                    hasSelectedDefaultVariant = true
                    var defaultVariantIdOptionMap = LinkedHashMap<Int, Int>()
                    for (optionId: Int in child.optionIds) {
                        for (variant: Variant in expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].variants) {
                            for (option: Option in variant.options) {
                                if (optionId == option.id) {
                                    defaultVariantIdOptionMap.put(variant.productVariantId, optionId)
                                }
                            }
                        }
                    }
                    checkoutVariantProductViewModel.selectedVariantOptionsIdMap = defaultVariantIdOptionMap
                } else {
                    productChild.isSelected = false
                }
                productChildList.add(productChild)
            }
        }
        checkoutVariantProductViewModel.productChildrenList = productChildList

        if (productChildList.isNotEmpty()) {
            var firstVariantId = 0
            var firstOptionId = 0
            for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                if (firstVariantId == 0 && firstOptionId == 0) {
                    firstVariantId = key
                    firstOptionId = value
                    break
                }
            }

            for (variantTypeViewModel: TypeVariantViewModel in typeVariantViewModels) {
                if (variantTypeViewModel.variantId != firstVariantId) {
                    for (variantOptionViewModel: OptionVariantViewModel in variantTypeViewModel.variantOptions) {
                        var hasAvailableChild = false
                        for (productChild: ProductChild in checkoutVariantProductViewModel.productChildrenList) {
                            if (productChild.isAvailable && variantOptionViewModel.optionId in productChild.optionsId &&
                                    firstOptionId in productChild.optionsId) {
                                hasAvailableChild = true
                                break
                            }
                        }
                        if (!hasAvailableChild) {
                            variantOptionViewModel.hasAvailableChild = false
                            variantOptionViewModel.currentState == variantOptionViewModel.STATE_NOT_AVAILABLE
                        } else {
                            variantOptionViewModel.hasAvailableChild = true
                            variantOptionViewModel.currentState == variantOptionViewModel.STATE_NOT_SELECTED
                        }
                    }
                }
            }
        }

        return checkoutVariantProductViewModel
    }

    override fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): ProfileViewModel {
        val userProfile: UserProfile? = expressCheckoutFormData.userProfileDefault
        var checkoutVariantProfileViewModel = ProfileViewModel()
        checkoutVariantProfileViewModel.addressTitle = userProfile?.receiverName ?: ""
        checkoutVariantProfileViewModel.addressDetail = userProfile?.addressStreet ?: ""
        checkoutVariantProfileViewModel.paymentOptionImageUrl = userProfile?.image ?: ""
        checkoutVariantProfileViewModel.paymentDetail = userProfile?.gatewayCode ?: ""

        return checkoutVariantProfileViewModel
    }

    override fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData,
                                            productViewModel: ProductViewModel): QuantityViewModel {
        var checkoutVariantQuantityViewModel = QuantityViewModel()
        checkoutVariantQuantityViewModel.errorFieldBetween = expressCheckoutFormData.messages?.errorFieldBetween ?: ""
        checkoutVariantQuantityViewModel.errorFieldMaxChar = expressCheckoutFormData.messages?.errorFieldMaxChar ?: ""
        checkoutVariantQuantityViewModel.errorFieldRequired = expressCheckoutFormData.messages?.errorFieldRequired ?: ""
        checkoutVariantQuantityViewModel.errorProductAvailableStock = expressCheckoutFormData.messages?.errorProductAvailableStock ?: ""
        checkoutVariantQuantityViewModel.errorProductAvailableStockDetail = expressCheckoutFormData.messages?.errorProductAvailableStockDetail ?: ""
        checkoutVariantQuantityViewModel.errorProductMaxQuantity = expressCheckoutFormData.messages?.errorProductMaxQuantity ?: ""
        checkoutVariantQuantityViewModel.errorProductMinQuantity = expressCheckoutFormData.messages?.errorProductMinQuantity ?: ""
        checkoutVariantQuantityViewModel.isStateError = false

        checkoutVariantQuantityViewModel.maxOrderQuantity = productViewModel.maxOrderQuantity
        checkoutVariantQuantityViewModel.minOrderQuantity = productViewModel.minOrderQuantity
        checkoutVariantQuantityViewModel.orderQuantity = productViewModel.minOrderQuantity
        checkoutVariantQuantityViewModel.stockWording = ""

        return checkoutVariantQuantityViewModel
    }

    override fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): SummaryViewModel {
        var checkoutVariantSummaryViewModel = SummaryViewModel(null)

        return checkoutVariantSummaryViewModel
    }

    override fun convertToTypeVariantViewModel(variant: Variant, children: ArrayList<Child>): TypeVariantViewModel {
        var checkoutVariantTypeVariantViewModel = TypeVariantViewModel(null)

        var checkoutVariantOptionVariantViewModels = ArrayList<OptionVariantViewModel>()
        for (option: Option in variant.options) {
            checkoutVariantOptionVariantViewModels.add(convertToOptionVariantViewModel(option, variant.productVariantId, children))
        }
        checkoutVariantTypeVariantViewModel.variantId = variant.productVariantId
        checkoutVariantTypeVariantViewModel.variantOptions = checkoutVariantOptionVariantViewModels
        checkoutVariantTypeVariantViewModel.variantName = variant.variantName

        return checkoutVariantTypeVariantViewModel
    }

    override fun convertToOptionVariantViewModel(option: Option, variantId: Int, children: ArrayList<Child>): OptionVariantViewModel {
        var checkoutVariantOptionVariantViewModel = OptionVariantViewModel(null)
        checkoutVariantOptionVariantViewModel.variantId = variantId
        checkoutVariantOptionVariantViewModel.optionId = option.id
        checkoutVariantOptionVariantViewModel.variantHex = option.hex ?: ""
        checkoutVariantOptionVariantViewModel.variantName = option.value

        var hasAvailableChild = false
        for (child: Child in children) {
            if (child.isBuyable && child.optionIds.contains(checkoutVariantOptionVariantViewModel.optionId)) {
                hasAvailableChild = true
                break
            }
        }
        checkoutVariantOptionVariantViewModel.hasAvailableChild = hasAvailableChild
        if (!hasAvailableChild) {
            checkoutVariantOptionVariantViewModel.currentState = checkoutVariantOptionVariantViewModel.STATE_NOT_AVAILABLE
        }

        return checkoutVariantOptionVariantViewModel
    }

    fun validateVariantCombination(productVariantData: ProductVariantData): Boolean {
        var variantOptionSizeList: ArrayList<Int> = ArrayList()
        for (variant: Variant in productVariantData.variants) {
            variantOptionSizeList.add(variant.options.size)
        }

        var variantCombinationSize: Int = 1
        for (optionSize: Int in variantOptionSizeList) {
            variantCombinationSize *= optionSize
        }

        return variantCombinationSize == productVariantData.children.size
    }

    fun validateVariantChildren(productVariantData: ProductVariantData): Boolean {

        for (child: Child in productVariantData.children) {
            if (child.optionIds.size != productVariantData.variants.size) {
                return false
            }
        }

        var hasValidVariant = false
        for (child: Child in productVariantData.children) {
            if (child.isBuyable) {
                hasValidVariant = true
                break
            }
        }

        return hasValidVariant
    }

}