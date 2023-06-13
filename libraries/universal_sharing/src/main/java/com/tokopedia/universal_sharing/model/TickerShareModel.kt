package com.tokopedia.universal_sharing.model

interface TickerShareModel {
    val imageResDrawable: Int
    val title: String
    val description: String
    val callback: () -> Unit
}
