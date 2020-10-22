package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressDetail(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("product_schedule_id")
        @Expose
        var productScheduleId: String = "",

        @SerializedName("product_schedule_package_id")
        @Expose
        var productSchedulePackageId: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("address")
        @Expose
        var address: String = "",

        @SerializedName("city")
        @Expose
        var city: String = "",

        @SerializedName("district")
        @Expose
        var district: String = "",

        @SerializedName("state")
        @Expose
        var state: String = "",

        @SerializedName("latitude")
        @Expose
        var latitude: String = "",

        @SerializedName("longitude")
        @Expose
        var longitude: String = "",

        @SerializedName("status")
        @Expose
        var status: Int = 0,

        @SerializedName("created_at")
        @Expose
        var createdAt: String = "",

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String = ""
) : Parcelable