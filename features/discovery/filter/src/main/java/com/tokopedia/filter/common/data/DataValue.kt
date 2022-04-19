package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class DataValue(@SerializedName("filter")
                @Expose
                var filter: List<Filter> = ArrayList(),

                @SerializedName("sort")
                @Expose
                var sort: List<Sort> = ArrayList()) : Parcelable
