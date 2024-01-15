package com.tokopedia.deals.ui.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meta(
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("webURL")
        @Expose
        var webURL: String = "",
        @SerializedName("appUR")
        @Expose
        var appUR: String = ""
) : Parcelable
