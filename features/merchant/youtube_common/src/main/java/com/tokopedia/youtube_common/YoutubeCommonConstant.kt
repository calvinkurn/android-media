package com.tokopedia.youtube_common

import com.tokopedia.url.TokopediaUrl

object YoutubeCommonConstant {
    val ENDPOINT_URL =  "${TokopediaUrl.getInstance().GOLDMERCHANT}youtube/v3/videos"
    const val ID_KEY = "id"
    const val PART_KEY = "part"
    const val PART_VALUE = "snippet"
}