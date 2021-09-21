package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 30/03/21
 */
data class OpenApplinkUiModel(
        val applink: String,
        val params: List<String> = emptyList(),
        val requestCode: Int? = null,
        val shouldFinish: Boolean = false
)