package com.tokopedia.shop.flashsale.presentation.draft.uimodel

import java.util.Date

data class DraftItemModel(
    val id: Long,
    val title: String = "",
    val description: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date()
)
