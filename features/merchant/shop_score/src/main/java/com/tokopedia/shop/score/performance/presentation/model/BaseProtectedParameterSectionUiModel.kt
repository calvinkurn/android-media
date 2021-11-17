package com.tokopedia.shop.score.performance.presentation.model

open class BaseProtectedParameterSectionUiModel(
    open val itemProtectedParameterList: List<ItemProtectedParameterUiModel> = emptyList(),
    open val titleParameterRelief: String = "",
    open val descParameterRelief: String = "",
    open val descParameterReliefBottomSheet: String = ""
)

data class ItemProtectedParameterUiModel(
    val parameterTitle: String = ""
)