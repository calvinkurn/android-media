package com.tokopedia.deals.common.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventSearch(
        @SerializedName("brands")
        @Expose
        var brands: List<Brand> = arrayListOf(),

        @SerializedName("products")
        @Expose
        var products: List<EventProductDetail> = arrayListOf()
) : Parcelable