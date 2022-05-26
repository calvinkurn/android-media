package com.tokopedia.shop.flash_sale.presentation.draft.uimodel

import java.util.Date

data class DraftItemModel(
    val id: String,
    val title: String,
    val description: String,
    val startDate: Date,
    val endDate: Date
)
