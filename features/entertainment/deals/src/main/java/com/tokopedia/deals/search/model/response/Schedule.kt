package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Schedule(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("provider_schedule_id")
        @Expose
        var providerScheduleId: String = "",

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("start_date")
        @Expose
        var startDate: String = "",

        @SerializedName("end_date")
        @Expose
        var endDate: String = "",

        @SerializedName("tnc")
        @Expose
        var tnc: String = "",

        @SerializedName("provider_meta_data")
        @Expose
        var providerMetaData: String = "",

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