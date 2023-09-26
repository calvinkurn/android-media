package com.tokopedia.filter.common.data

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Search(@SerializedName("searchable")
             @Expose
             var searchable: Int = 0,
             @SerializedName("placeholder")
             @Expose
             var placeholder: String = "") : Parcelable
