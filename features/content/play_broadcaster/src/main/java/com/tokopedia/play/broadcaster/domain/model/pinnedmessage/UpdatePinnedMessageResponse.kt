package com.tokopedia.play.broadcaster.domain.model.pinnedmessage

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 12/10/21
 */
data class UpdatePinnedMessageResponse(
    @SerializedName("broadcasterUpdatePinMessage")
    val data: Data = Data(),
) {

    data class Data(
        @SerializedName("ID")
        val id: String = ""
    )
}