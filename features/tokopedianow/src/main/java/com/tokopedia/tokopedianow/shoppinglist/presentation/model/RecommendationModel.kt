package com.tokopedia.tokopedianow.shoppinglist.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE

data class RecommendationModel(
    var pageCounter: Int = Int.ONE,
    var hasNext: Boolean = false,
    var title: String = String.EMPTY
)
