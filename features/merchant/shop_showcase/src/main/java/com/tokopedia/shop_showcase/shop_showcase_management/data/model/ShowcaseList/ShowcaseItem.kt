package com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowcaseItem(
        @SerializedName("aceDefaultSort")
        var aceDefaultSort: Int = 0,
        @SerializedName("alias")
        var alias: String = "",
        @SerializedName("badge")
        var badge: String = "",
        @SerializedName("count")
        var count: Int = 0,
        @SerializedName("highlighted")
        var highlighted: Boolean = false,
        @SerializedName("id")
        var id: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("uri")
        var uri: String = "",
        @SerializedName("useAce")
        var useAce: Boolean = false,
        var isChecked: Boolean = false
): Parcelable {

}

