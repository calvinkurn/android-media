package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantNoteViewModel

    fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProductViewModel

    fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProfileViewModel

    fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantQuantityViewModel

    fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantSummaryViewModel

    fun convertToTypeVariantViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantTypeVariantViewModel

    fun convertToOptionVariantViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantOptionVariantViewModel

}