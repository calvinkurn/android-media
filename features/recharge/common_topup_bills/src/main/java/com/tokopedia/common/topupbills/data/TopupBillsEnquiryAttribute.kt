package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
data class TopupBillsEnquiryAttribute (
    @SerializedName("UserID")
    @Expose
    val userId: String = "",
    @SerializedName("ProductID")
    @Expose
    val productId: String = "",
    @SerializedName("ClientNumber")
    @Expose
    val clientNumber: String = "",
    @SerializedName("Price")
    @Expose
    val price: String = "",
    @SerializedName("PricePlain")
    @Expose
    val pricePlain: Int = 0,
    @SerializedName("mainInfo")
    @Expose
    val mainInfoList: List<TopupBillsEnquiryMainInfo> = listOf()
)