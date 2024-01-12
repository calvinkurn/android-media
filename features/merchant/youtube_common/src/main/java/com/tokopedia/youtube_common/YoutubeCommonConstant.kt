package com.tokopedia.youtube_common

import com.tokopedia.url.TokopediaUrl

object YoutubeCommonConstant {
    val ENDPOINT_URL =  "${TokopediaUrl.getInstance().API}shop-layout-public/youtube/v3/videos"
    val ENDPOINT_TOKOPEDIA = "https://tokopedia.com/"
    const val ID_KEY = "id"
    const val PART_KEY = "part"
    const val PART_VALUE = "snippet"
}
