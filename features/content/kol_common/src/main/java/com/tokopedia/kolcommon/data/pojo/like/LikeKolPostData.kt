package com.tokopedia.kolcommon.data.pojo.like

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-05
 */
data class LikeKolPostData(
        @SerializedName("do_like_kol_post")
        val doLikeKolPost: DoLikeKolPost,
) {
        data class DoLikeKolPost(
                @SerializedName("error")
                val error: String = "",
                @SerializedName("data")
                val data: LikeKolPostSuccessData = LikeKolPostSuccessData(),
        )

        data class LikeKolPostSuccessData(
                @SerializedName("success")
                val success: Int = 0
        )
}