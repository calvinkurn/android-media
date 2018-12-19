package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutFormData
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.Variant
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToViewModels(expressCheckoutFormData: ExpressCheckoutFormData): ArrayList<Visitable<*>>

    fun convertToNoteViewModel(expressCheckoutFormData: ExpressCheckoutFormData): NoteViewModel

    fun convertToProductViewModel(expressCheckoutFormData: ExpressCheckoutFormData,
                                  typeVariantViewModels: ArrayList<TypeVariantViewModel>):
            ProductViewModel

    fun convertToProfileViewModel(expressCheckoutFormData: ExpressCheckoutFormData): ProfileViewModel

    fun convertToQuantityViewModel(expressCheckoutFormData: ExpressCheckoutFormData,
                                   productViewModel: ProductViewModel):
            QuantityViewModel

    fun convertToSummaryViewModel(expressCheckoutFormData: ExpressCheckoutFormData): SummaryViewModel

    fun convertToTypeVariantViewModel(variant: Variant, children: ArrayList<Child>): TypeVariantViewModel

    fun convertToOptionVariantViewModel(option: Option, variantId: Int, children: ArrayList<Child>): OptionVariantViewModel

}