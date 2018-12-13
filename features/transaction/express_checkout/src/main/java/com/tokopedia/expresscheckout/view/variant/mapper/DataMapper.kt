package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.transactiondata.entity.response.variant.Option
import com.tokopedia.transactiondata.entity.response.variant.Variant
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToViewModels(expressCheckoutFormData: ExpressCheckoutFormData): ArrayList<Visitable<*>>

    fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantNoteViewModel

    fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProductViewModel

    fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantProfileViewModel

    fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantQuantityViewModel

    fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): CheckoutVariantSummaryViewModel

    fun convertToTypeVariantViewModel(variant: Variant): CheckoutVariantTypeVariantViewModel

    fun convertToOptionVariantViewModel(option: Option): CheckoutVariantOptionVariantViewModel

}