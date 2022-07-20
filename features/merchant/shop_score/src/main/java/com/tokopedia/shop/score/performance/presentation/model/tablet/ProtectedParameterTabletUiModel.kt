package com.tokopedia.shop.score.performance.presentation.model.tablet

import com.tokopedia.shop.score.performance.presentation.adapter.tablet.DetailPerformanceAdapterTabletTypeFactory
import com.tokopedia.shop.score.performance.presentation.model.BaseProtectedParameterSectionUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemProtectedParameterUiModel

data class ProtectedParameterTabletUiModel(
    override val itemProtectedParameterList: List<ItemProtectedParameterUiModel> = emptyList(),
    override val titleParameterRelief: Int? = null,
    override val descParameterRelief: Int? = null,
    override val descParameterReliefBottomSheet: Int? = null,
    override val protectedParameterDaysDate: String = ""
) : BaseProtectedParameterSectionUiModel(
    itemProtectedParameterList,
    titleParameterRelief,
    descParameterRelief,
    descParameterReliefBottomSheet,
    protectedParameterDaysDate
), BaseParameterDetail {
    override fun type(typeFactory: DetailPerformanceAdapterTabletTypeFactory): Int {
        return typeFactory.type(this)
    }
}