package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayUiModel(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("channelId")
    val channelId: String = "",
    @SerializedName("warehouseId")
    val warehouseId: String = "",
    @SerializedName("gcToken")
    val gcToken: String = "",
    @SerializedName("header")
    val header: String = "",
) : Parcelable
