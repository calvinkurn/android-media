package com.tokopedia.search.result.presentation.model

import com.tokopedia.discovery.common.constants.SearchConstant

data class InspirationCardOptionViewModel(
        val text: String = "",
        val img: String = "",
        val url: String = "",
        val color: String = "",
        val applink: String = "",
        val inspirationCardType: String = ""
) {

    fun isRelated() = inspirationCardType == SearchConstant.InspirationCard.TYPE_RELATED
}