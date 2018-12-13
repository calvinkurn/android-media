package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.expresscheckout.domain.entity.UserProfile
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
        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToProfileViewModel(expressCheckoutFormData))
        }
        dataList.add(convertToProductViewModel(expressCheckoutFormData))
        dataList.add(convertToQuantityViewModel(expressCheckoutFormData))
        val variantCombinationValidation = validateVariantCombination(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
        val variantChildrenValidation = validateVariantChildren(expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0])
        if (variantCombinationValidation && variantChildrenValidation) {
            for (variant: Variant in expressCheckoutFormData.cart.groupShops[0].products[0].productVariantData[0].variants) {
                dataList.add(convertToTypeVariantViewModel(variant))
            }
        }
        dataList.add(convertToNoteViewModel(expressCheckoutFormData))
        if (expressCheckoutFormData.userProfileDefault != null) {
            dataList.add(convertToSummaryViewModel(expressCheckoutFormData))
        }

        return dataList
    }

    override fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantNoteViewModel {
        var checkoutVariantNoteViewModel = CheckoutVariantNoteViewModel()
        checkoutVariantNoteViewModel.noteCharMax = 144
        checkoutVariantNoteViewModel.note = ""

        return checkoutVariantNoteViewModel
    }

    override fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProductViewModel {
        val product: Product = expressCheckoutFormData.cart.groupShops[0].products[0]
        var checkoutVariantProductViewModel = CheckoutVariantProductViewModel()
        checkoutVariantProductViewModel.productImageUrl = product.productImageSrc200Square
        checkoutVariantProductViewModel.productName = product.productName
        checkoutVariantProductViewModel.productPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.productPrice, false)

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

    override fun convertToTypeVariantViewModel(variant: Variant): CheckoutVariantTypeVariantViewModel {
        var checkoutVariantTypeVariantViewModel = CheckoutVariantTypeVariantViewModel(null)

        var checkoutVariantOptionVariantViewModels = ArrayList<CheckoutVariantOptionVariantViewModel>()
        for (option: Option in variant.options) {
            checkoutVariantOptionVariantViewModels.add(convertToOptionVariantViewModel(option))
        }
        checkoutVariantTypeVariantViewModel.variantOptions = checkoutVariantOptionVariantViewModels
        checkoutVariantTypeVariantViewModel.variantName = variant.variantName
        for (variantOption: CheckoutVariantOptionVariantViewModel in checkoutVariantTypeVariantViewModel.variantOptions) {
            if (variantOption.currentState == variantOption.STATE_SELECTED) {
                checkoutVariantTypeVariantViewModel.variantSelectedValue = variantOption.variantName
                break
            }
        }

        return checkoutVariantTypeVariantViewModel
    }

    override fun convertToOptionVariantViewModel(option: Option): CheckoutVariantOptionVariantViewModel {
        var checkoutVariantOptionVariantViewModel = CheckoutVariantOptionVariantViewModel(null)
        checkoutVariantOptionVariantViewModel.variantHex = option.hex
        checkoutVariantOptionVariantViewModel.variantName = option.value

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

        return true
    }

}