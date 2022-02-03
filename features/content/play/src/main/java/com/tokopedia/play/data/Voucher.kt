package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
class Voucher (
        @SerializedName("ID")
        val id: String = "",
        @SerializedName("ShopID")
        val shopId: String = "",
        @SerializedName("VoucherType")
        val voucherType: Int = 0,
        @SerializedName("Name")
        val voucherName: String = "",
        @SerializedName("VoucherImage")
        val voucherImage: String = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("Subtitle")
        val subtitle: String = "",
        @SerializedName("VoucherQuota")
        val quota: Int = 0,
        @SerializedName("VoucherFinishTime")
        val finishTime: String = "", // formatted string date
        @SerializedName("VoucherCode")
        val code: String = "",
        @SerializedName("VoucherImageSquare")
        val imageSquare: String = "",
        @SerializedName("IsQuotaAvailable")
        val isAvailable: Int = 0,
        @SerializedName("tnc")
        val termAndCondition: String = "",
        @SerializedName("IsVoucherCopyable")
        val isCopyable: Boolean = false,
        @SerializedName("IsHighlighted")
        val isHighlighted: Boolean = false,
        @SerializedName("IsPrivate")
        val isPrivate: Boolean = false

)