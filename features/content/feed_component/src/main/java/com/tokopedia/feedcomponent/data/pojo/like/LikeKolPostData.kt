package com.tokopedia.feedcomponent.data.pojo.like

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-05
 */
data class LikeKolPostData(
        @SerializedName("do_like_kol_post")
        val doLikeKolPost: DoLikeKolPost
)