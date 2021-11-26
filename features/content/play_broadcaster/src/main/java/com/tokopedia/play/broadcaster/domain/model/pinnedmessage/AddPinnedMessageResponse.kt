package com.tokopedia.play.broadcaster.domain.model.pinnedmessage

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 12/10/21
 */
data class AddPinnedMessageResponse(
    @SerializedName("broadcasterAddPinMessages")
    val data: Data = Data(),
) {

    data class Data(
        @SerializedName("messageIDs")
        val messageIds: List<String> = emptyList()
    )
}