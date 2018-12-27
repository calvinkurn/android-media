package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutFormData
import com.tokopedia.expresscheckout.domain.model.AtcExpressCheckoutModel
import com.tokopedia.expresscheckout.domain.model.ChildModel
import com.tokopedia.expresscheckout.domain.model.OptionModel
import com.tokopedia.expresscheckout.domain.model.VariantModel
import com.tokopedia.transactiondata.entity.response.variantdata.Option
import com.tokopedia.transactiondata.entity.response.variantdata.Variant
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.transactiondata.entity.response.variantdata.Child

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToViewModels(atcExpressCheckoutModel: AtcExpressCheckoutModel): ArrayList<Visitable<*>>

    fun convertToNoteViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): NoteViewModel

    fun convertToProductViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel,
                                  typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel

    fun convertToProfileViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): ProfileViewModel

    fun convertToQuantityViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel,
                                   productViewModel: ProductViewModel): QuantityViewModel

    fun convertToSummaryViewModel(atcExpressCheckoutModel: AtcExpressCheckoutModel): SummaryViewModel

    fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel

    fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel

    fun convertToInsuranceViewModel(): InsuranceViewModel
}