package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

private const val KEY_POSITION = "position"
private const val KEY_TYPE = "type"
private const val KEY_TITLE = "title"
private const val KEY_URL = "url"

@Parcelize
data class LabelGroup(
    @SerializedName(KEY_POSITION)
    @Expose
    var position: String = "",

    @SerializedName(KEY_TYPE)
    @Expose
    var type: String = "",

    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_URL)
    @Expose
    var imageUrl: String = ""
) : Parcelable
