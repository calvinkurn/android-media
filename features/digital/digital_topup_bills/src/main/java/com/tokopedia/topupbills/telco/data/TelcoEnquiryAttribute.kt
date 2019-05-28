package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class TelcoEnquiryAttribute (
    @SerializedName("UserID")
    @Expose
    val userId: String,
    @SerializedName("ProductID")
    @Expose
    val productId: String,
    @SerializedName("Price")
    @Expose
    val price: String,
    @SerializedName("PricePlain")
    @Expose
    val pricePlain: Int,
    @SerializedName("mainInfo")
    @Expose
    val mainInfoList: List<TelcoEnquiryMainInfo>
)