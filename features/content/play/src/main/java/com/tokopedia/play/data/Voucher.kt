package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
class Voucher (
        @SerializedName("voucher_id")
        val id: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("voucher_type")
        val voucherType: Int = 0,
        @SerializedName("voucher_name")
        val voucherName: String = "",
        @SerializedName("voucher_image")
        val voucherImage: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("subtitle")
        val subtitle: String = "",
        @SerializedName("voucher_quota")
        val quota: Int = 0,
        @SerializedName("voucher_finish_time")
        val finishTime: String = "", // formatted string date
        @SerializedName("voucher_code")
        val code: String = "",
        @SerializedName("voucher_image_square")
        val imageSquare: String = "",
        @SerializedName("is_quota_available")
        val isAvailable: Int = 0,
        @SerializedName("tnc")
        val termAndCondition: String = "",
        @SerializedName("is_copyable")
        val isCopyable: Boolean = false,
        @SerializedName("is_highlighted")
        val isHighlighted: Boolean = false,
        @SerializedName("is_private")
        val isPrivate: Boolean = false

)