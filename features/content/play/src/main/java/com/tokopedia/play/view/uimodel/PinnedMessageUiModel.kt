package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 16/12/19
 */
data class PinnedMessageUiModel(
        val applink: String,
        val title: String,
        val message: String,
        val shouldRemove: Boolean
)