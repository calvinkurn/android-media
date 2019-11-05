package com.tokopedia.kol.feature.post.data.pojo.likekolpost

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-05
 */
data class LikeKolPostData(
        @SerializedName("do_like_kol_post")
        val doLikeKolPost: DoLikeKolPost
)