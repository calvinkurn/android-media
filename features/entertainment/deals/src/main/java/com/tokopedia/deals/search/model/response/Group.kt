package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("product_schedule_id")
        @Expose
        var productScheduleId: String = "",

        @SerializedName("provider_group_id")
        @Expose
        var providerGroupId: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("tnc")
        @Expose
        var tnc: String = "",

        @SerializedName("provider_meta_data")
        @Expose
        var providerMetaData: String = "",

        @SerializedName("provider_application")
        @Expose
        var providerApplication: String = "",

        @SerializedName("provider_is_full_layout")
        @Expose
        var providerIsFullLayout: String = "",

        @SerializedName("status")
        @Expose
        var status: Int = 0,

        @SerializedName("created_at")
        @Expose
        var createdAt: String = "",

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String = "",

        @SerializedName("packages")
        @Expose
        var packages: Packages = Packages()
): Parcelable