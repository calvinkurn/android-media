package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

private const val KEY_TITLE = "title"
const val KEY_COLOR = "color"

@Parcelize
data class Label(
    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_COLOR)
    @Expose
    var color: String? = ""
) : Parcelable
