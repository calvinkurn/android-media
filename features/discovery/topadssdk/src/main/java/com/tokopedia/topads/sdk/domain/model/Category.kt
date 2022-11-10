package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

private const val KEY_ID = "id"

@Parcelize
data class Category(
    @SerializedName(KEY_ID)
    var id: Int = 0
) : Parcelable
