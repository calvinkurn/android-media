package com.tokopedia.digital_product_detail.data.model.param

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteNumberParam(
    @SerializedName("source")
    @Expose
    var source: String? = null,
    @SerializedName("category_ids")
    @Expose
    var categoryIds: List<String>? = null,
    @SerializedName("min_last_transaction")
    @Expose
    var minLastTransaction: String? = null,
    @SerializedName("min_total_transaction")
    @Expose
    var minTotalTransaction: String? = null,
    @SerializedName("service_plan_type")
    @Expose
    var servicePlanType: String? = null,
    @SerializedName("subscription")
    @Expose
    var subscription: Boolean? = null,
    @SerializedName("limit")
    @Expose
    var limit: Int? = null,
): Parcelable