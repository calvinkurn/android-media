package com.tokopedia.play.broadcaster.domain.model.interactive

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 06/07/21.
 */
data class PostInteractiveCreateSessionResponse(
    @SerializedName("playInteractiveSellerCreateSession")
    val interactiveSellerCreateSession: InteractiveSellerCreateSession = InteractiveSellerCreateSession()
) {

    data class InteractiveSellerCreateSession(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val data: Data = Data()
    )

    data class Header(
        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("message")
        val message: String = ""
    )

    data class Data(
        @SerializedName("interactiveID")
        val interactiveId: String = ""
    )

}