package com.tokopedia.topads.dashboard.data.model.beranda

data class SummaryBeranda(
    val title: String,
    val id: Int,
    val count: Int,
    val percentCount: String,
    val selectedColor: Int,
) {
    var isSelected: Boolean = false
}