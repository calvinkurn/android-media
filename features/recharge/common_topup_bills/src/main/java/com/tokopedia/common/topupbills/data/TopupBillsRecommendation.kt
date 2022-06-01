package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 28/05/19.
 */
@Parcelize
class TopupBillsRecommendation(
        @SerializedName("iconUrl")
        @Expose
        val iconUrl: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("clientNumber")
        @Expose
        val clientNumber: String = "",
        @SerializedName("appLink")
        @Expose
        val applink: String = "",
        @SerializedName("webLink")
        @Expose
        val weblink: String = "",
        @SerializedName("productPrice")
        @Expose
        val productPrice: Int = 0,
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("categoryId")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("productId")
        @Expose
        val productId: Int = 0,
        @SerializedName("isATC")
        @Expose
        val isAtc: Boolean = false,
        @SerializedName("operatorID")
        @Expose
        val operatorId: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = "",
        var position: Int = 0
) : Parcelable