package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 12/06/2020
 */
data class GetProductV3Response(
    @SerializedName("getProductV3")
    val getProductV3: GetProductV3
) {

    data class GetProductV3(
        @SerializedName("pictures")
        val pictures: List<Picture>
    )

    data class Picture(
        @SerializedName("urlOriginal")
        val urlOriginal: String
    )
}