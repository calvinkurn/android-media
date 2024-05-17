package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val KEY_ID = "id"

@Parcelize
data class Category(
    @SerializedName(KEY_ID)
    var id: Int = 0
) : Parcelable
