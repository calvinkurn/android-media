package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class ViewModelMapper : DataMapper {

    override fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantNoteViewModel {
        var checkoutVariantNoteViewModel = CheckoutVariantNoteViewModel()

        return checkoutVariantNoteViewModel
    }

    override fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProductViewModel {
        var checkoutVariantProductViewModel = CheckoutVariantProductViewModel()

        return checkoutVariantProductViewModel
    }

    override fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProfileViewModel {
        var checkoutVariantProfileViewModel = CheckoutVariantProfileViewModel()

        return checkoutVariantProfileViewModel
    }

    override fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantQuantityViewModel {
        var checkoutVariantQuantityViewModel = CheckoutVariantQuantityViewModel()

        return checkoutVariantQuantityViewModel
    }

    override fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantSummaryViewModel {
        var checkoutVariantSummaryViewModel = CheckoutVariantSummaryViewModel()

        return checkoutVariantSummaryViewModel
    }

    override fun convertToTypeVariantViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantTypeVariantViewModel {
        var checkoutVariantTypeVariantViewModel = CheckoutVariantTypeVariantViewModel()

        return checkoutVariantTypeVariantViewModel
    }

    override fun convertToOptionVariantViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantOptionVariantViewModel {
        var checkoutVariantOptionVariantViewModel = CheckoutVariantOptionVariantViewModel()

        return checkoutVariantOptionVariantViewModel
    }

}