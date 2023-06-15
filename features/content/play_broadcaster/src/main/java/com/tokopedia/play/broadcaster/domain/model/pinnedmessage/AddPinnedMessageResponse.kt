package com.tokopedia.play.broadcaster.domain.model.pinnedmessage

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 12/10/21
 */
data class AddPinnedMessageResponse(
    @SerializedName("broadcasterAddPinMessages")
    val data: Data = Data(),
) {

    data class Data(
        @SuppressLint("Invalid Data Type")
        @SerializedName("messageIDs")
        val messageIds: List<String> = emptyList()
    )
}
