package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
data class PlayShareInfoUiModel(
        val content: String = "",
        val shouldShow: Boolean = false,
        val redirectUrl: String = "",
        val textDescription: String = "",
        val metaTitle: String = "",
        val metaDescription: String = "",
)