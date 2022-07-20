package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class ItemParameterFaqUiModel(
    @StringRes val title: Int? = null,
    @StringRes val desc: Int? = null,
    @StringRes var score: Int? = null
)