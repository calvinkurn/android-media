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

        val variantCombinationValidation = validateVariantCombination(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
        val variantChildrenValidation = validateVariantChildren(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
        var hasVariant = variantCombinationValidation && variantChildrenValidation
        var variantViewModelList = ArrayList<CheckoutVariantTypeVariantViewModel>()
        var children = expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].children
        if (hasVariant) {
            for (variant: Variant in expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].variants) {
                variantViewModelList.add(convertToTypeVariantViewModel(variant, children))
            }
        }

        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToProfileViewModel(expressCheckoutFormData))
        }
        dataList.add(convertToProductViewModel(expressCheckoutFormData, variantViewModelList))
        dataList.add(convertToQuantityViewModel(expressCheckoutFormData))
        dataList.addAll(variantViewModelList)
        dataList.add(convertToNoteViewModel(expressCheckoutFormData))
        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToSummaryViewModel(expressCheckoutFormData))
        }

        return dataList
    }

    override fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantNoteViewModel {
        var checkoutVariantNoteViewModel = CheckoutVariantNoteViewModel()
        checkoutVariantNoteViewModel.noteCharMax = expressCheckoutFormData.maxCharNote ?: 144
        checkoutVariantNoteViewModel.note = ""

        return checkoutVariantNoteViewModel
    }

    override fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData,
                                           checkoutVariantTypeVariantViewModels: ArrayList<CheckoutVariantTypeVariantViewModel>): CheckoutVariantProductViewModel {
        val product: Product = expressCheckoutFormData.cart.groupShops[0].products[0]
        var checkoutVariantProductViewModel = CheckoutVariantProductViewModel()
        checkoutVariantProductViewModel.productImageUrl = product.productImageSrc200Square
        checkoutVariantProductViewModel.productName = product.productName
        checkoutVariantProductViewModel.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.productPrice, false)
        var productChildList = ArrayList<ProductChild>()
        var hasSelectedDefaultVariant = false
        if (checkoutVariantTypeVariantViewModels.size > 0) {
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
                    var defaultVariantIdOptionMap = HashMap<Int, Int>()
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
                }
                productChildList.add(productChild)
/*
                for (optionId: Int in child.optionIds) {
                    for (checkoutVariantTypeVariantViewModel: CheckoutVariantTypeVariantViewModel in checkoutVariantTypeVariantViewModels) {
                        for (variantOption: CheckoutVariantOptionVariantViewModel in checkoutVariantTypeVariantViewModel.variantOptions) {
                            if (variantOption.id == optionId) {
                                when {
                                    productChild.isSelected && child.optionIds.contains(variantOption.id) -> {
                                        variantOption.currentState = variantOption.STATE_SELECTED
                                        checkoutVariantTypeVariantViewModel.variantSelectedValue = variantOption.variantName
                                    }
                                    !productChild.isAvailable -> variantOption.currentState = variantOption.STATE_NOT_AVAILABLE
                                    else -> variantOption.currentState = variantOption.STATE_NOT_SELECTED
                                }
                            }
                        }
                    }
                }
*/

            }
        }
        checkoutVariantProductViewModel.productChildrenList = productChildList

        return checkoutVariantProductViewModel
    }

    override fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProfileViewModel {
        val userProfile: UserProfile? = expressCheckoutFormData.userProfileDefault
        var checkoutVariantProfileViewModel = CheckoutVariantProfileViewModel()
        checkoutVariantProfileViewModel.addressTitle = userProfile?.receiverName ?: ""
        checkoutVariantProfileViewModel.addressDetail = userProfile?.addressStreet ?: ""
        checkoutVariantProfileViewModel.paymentOptionImageUrl = userProfile?.image ?: ""
        checkoutVariantProfileViewModel.paymentDetail = userProfile?.gatewayCode ?: ""

        return checkoutVariantProfileViewModel
    }

    override fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantQuantityViewModel {
        var checkoutVariantQuantityViewModel = CheckoutVariantQuantityViewModel()

        return checkoutVariantQuantityViewModel
    }

    override fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantSummaryViewModel {
        var checkoutVariantSummaryViewModel = CheckoutVariantSummaryViewModel(null)

        return checkoutVariantSummaryViewModel
    }

    override fun convertToTypeVariantViewModel(variant: Variant, children: ArrayList<Child>): CheckoutVariantTypeVariantViewModel {
        var checkoutVariantTypeVariantViewModel = CheckoutVariantTypeVariantViewModel(null)

        var checkoutVariantOptionVariantViewModels = ArrayList<CheckoutVariantOptionVariantViewModel>()
        for (option: Option in variant.options) {
            checkoutVariantOptionVariantViewModels.add(convertToOptionVariantViewModel(option, variant.productVariantId, children))
        }
        checkoutVariantTypeVariantViewModel.variantId = variant.productVariantId
        checkoutVariantTypeVariantViewModel.variantOptions = checkoutVariantOptionVariantViewModels
        checkoutVariantTypeVariantViewModel.variantName = variant.variantName

        return checkoutVariantTypeVariantViewModel
    }

    override fun convertToOptionVariantViewModel(option: Option, variantId: Int, children: ArrayList<Child>): CheckoutVariantOptionVariantViewModel {
        var checkoutVariantOptionVariantViewModel = CheckoutVariantOptionVariantViewModel(null)
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