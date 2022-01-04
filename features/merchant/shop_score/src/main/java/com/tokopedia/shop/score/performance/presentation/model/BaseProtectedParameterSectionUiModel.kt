package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

open class BaseProtectedParameterSectionUiModel(
    open val itemProtectedParameterList: List<ItemProtectedParameterUiModel> = emptyList(),
    @StringRes open val titleParameterRelief: Int? = null,
    @StringRes open val descParameterRelief: Int? = null,
    @StringRes open val descParameterReliefBottomSheet: Int? = null,
    open val protectedParameterDaysDate: String = ""
)

data class ItemProtectedParameterUiModel(
    val parameterTitle: String = ""
)