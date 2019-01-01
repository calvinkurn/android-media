package com.tokopedia.expresscheckout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.domain.model.atc.ChildModel
import com.tokopedia.expresscheckout.domain.model.atc.OptionModel
import com.tokopedia.expresscheckout.domain.model.atc.VariantModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToViewModels(atcResponseModel: AtcResponseModel): ArrayList<Visitable<*>>

    fun convertToNoteViewModel(atcResponseModel: AtcResponseModel): NoteViewModel

    fun convertToProductViewModel(atcResponseModel: AtcResponseModel,
                                  typeVariantViewModels: ArrayList<TypeVariantViewModel>): ProductViewModel

    fun convertToProfileViewModel(atcResponseModel: AtcResponseModel): ProfileViewModel

    fun convertToQuantityViewModel(atcResponseModel: AtcResponseModel,
                                   productViewModel: ProductViewModel): QuantityViewModel

    fun convertToSummaryViewModel(atcResponseModel: AtcResponseModel): SummaryViewModel

    fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantViewModel

    fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantViewModel

    fun convertToInsuranceViewModel(): InsuranceViewModel
}