package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

private const val KEY_HEADER = "header"
private const val KEY_STATUS = "status"
private const val KEY_DATA = "data"
private const val KEY_TEMPLATE = "template"
private const val KEY_ERROR = "errors"

@Parcelize
data class TopAdsModel(
    @SerializedName(KEY_ERROR)
    @Expose
    var error: Error = Error(),

    @SerializedName(KEY_STATUS)
    @Expose
    var status: Status = Status(),

    @SerializedName(KEY_HEADER)
    @Expose
    var header: Header? = Header(),

    @SerializedName(KEY_DATA)
    @Expose
    var data: MutableList<Data> = ArrayList(),

    @SerializedName(KEY_TEMPLATE)
    var templates: MutableList<Template> = ArrayList(),

    @Expose(deserialize = false, serialize = false)
    var adsPosition: Int = 0
) : Parcelable
