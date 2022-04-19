package com.tokopedia.common_category.model.bannedCategory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BannedData(
        @SerializedName("name")
        var name: String? = null,

        @SerializedName("bannedMsg")
        var bannedMessage: String? = null,

        @SerializedName("bannedMsgHeader")
        var bannedMsgHeader: String? = null,

        @SerializedName("appRedirection")
        var appRedirection: String? = null,

        @SerializedName("displayButton")
        var displayButton: Boolean = false,

        @SerializedName("isBanned")
        var isBanned: Int = 0) : Parcelable
