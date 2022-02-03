package com.tokopedia.topads.dashboard.data.model

data class Chip(
    val title: String,
    val adTypeId: String,
    var isSelected: Boolean = false
)