package com.tokopedia.common.topupbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.atc.data.response.AttributesCart
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 27/05/19.
 */
@Parcelize
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
    val mainInfoList: List<TopupBillsEnquiryMainInfo> = listOf(),
    @SerializedName("additionalInfo")
    @Expose
    val additionalMainInfo: List<AdditionalInfoInquiry> = listOf()
): Parcelable

@Parcelize
data class AdditionalInfoInquiry (
    @SerializedName("detail")
    @Expose
    val detail: List<TopupBillsEnquiryMainInfo> = listOf()
): Parcelable
