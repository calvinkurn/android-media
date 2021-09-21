package com.tokopedia.search.result.presentation.model

import com.tokopedia.discovery.common.constants.SearchConstant

data class InspirationCardOptionDataView(
        val text: String = "",
        val img: String = "",
        val url: String = "",
        val hexColor: String = "",
        val applink: String = "",
        val inspirationCardType: String = ""
) {

    fun isRelated() = inspirationCardType == SearchConstant.InspirationCard.TYPE_RELATED
}