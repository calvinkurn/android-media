package com.tokopedia.sellerappwidget.view.model

/**
 * Created By @ilhamsuaib on 27/11/20
 */

data class CommonStateUiModel(
        val widgetId: Int,
        val title: String,
        val description: String,
        val imgUrl: String,
        val appLink: String = "",
        val ctaText: String = ""
)