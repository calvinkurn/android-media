package com.tokopedia.smartbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DueUrgency(
        @SerializedName("Type")
        @Expose
        val type: Int = 0,
        @SerializedName("Text")
        @Expose
        val text: String = ""
): Parcelable
