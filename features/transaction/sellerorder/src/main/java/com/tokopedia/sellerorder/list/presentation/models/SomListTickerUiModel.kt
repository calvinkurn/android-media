package com.tokopedia.sellerorder.list.presentation.models

data class SomListTickerUiModel(
        val id: Long = 0,
        val body: String = "",
        val shortDesc: String = "",
        val isActive: Boolean = false
)