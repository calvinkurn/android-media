package com.tokopedia.tokomember_seller_dashboard.model

data class TmIntroBottomsheetModel(
    val title: String,
    val desc: String,
    val image: String,
    val ctaName: String = "",
    val type: String = "",
    val errorCount:Int = 0
)