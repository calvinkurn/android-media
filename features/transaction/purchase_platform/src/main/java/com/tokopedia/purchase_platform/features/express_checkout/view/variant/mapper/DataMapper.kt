package com.tokopedia.purchase_platform.features.express_checkout.view.variant.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.uimodel.*
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.AtcResponseModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.ChildModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.OptionModel
import com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc.VariantModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface DataMapper {

    fun convertToViewModels(atcResponseModel: AtcResponseModel, productData: ProductData?): ArrayList<Visitable<*>>

    fun convertToNoteViewModel(atcResponseModel: AtcResponseModel): NoteUiModel

    fun convertToProductViewModel(atcResponseModel: AtcResponseModel,
                                  typeVariantUiModels: ArrayList<TypeVariantUiModel>): ProductUiModel

    fun convertToProfileViewModel(atcResponseModel: AtcResponseModel): ProfileUiModel

    fun convertToQuantityViewModel(atcResponseModel: AtcResponseModel,
                                   productUiModel: ProductUiModel): QuantityUiModel

    fun convertToSummaryViewModel(atcResponseModel: AtcResponseModel): SummaryUiModel

    fun convertToTypeVariantViewModel(variantModel: VariantModel, childrenModel: ArrayList<ChildModel>): TypeVariantUiModel

    fun convertToOptionVariantViewModel(optionModel: OptionModel, variantId: Int, childrenModel: ArrayList<ChildModel>): OptionVariantUiModel

    fun convertToInsuranceViewModel(productData: ProductData, summaryUiModel: SummaryUiModel?): InsuranceUiModel
}