package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class TelcoAttributesProduct(
        @SerializedName("product_labels")
        @Expose
        var productLabels: List<String> = listOf(),
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = "",
        @SerializedName("detail_url")
        @Expose
        val detailUrl: String = "",
        @SerializedName("detail_url_text")
        @Expose
        val detailUrlText: String = "",
        @SerializedName("info")
        @Expose
        val info: String = "",
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("price_plain")
        @Expose
        val pricePlain: Int = 0,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("detail_compact")
        @Expose
        val detailCompact: String = "",
        @SerializedName("promo")
        @Expose
        val productPromo: TelcoProductPromo? = TelcoProductPromo(),
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("operator_id")
        @Expose
        val operatorId: Int = 0,
        var selected: Boolean = false)
    : Parcelable