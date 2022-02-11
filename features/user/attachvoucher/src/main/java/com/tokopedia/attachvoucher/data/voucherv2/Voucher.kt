package com.tokopedia.attachvoucher.data.voucherv2


import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("booked_global_quota")
    val bookedGlobalQuota: Int = 0,
    @SerializedName("create_by")
    val createBy: Int = 0,
    @SerializedName("create_time")
    val createTime: String = "",
    @SerializedName("galadriel_catalog_id")
    val galadrielCatalogId: Int = 0,
    @SerializedName("galadriel_voucher_id")
    val galadrielVoucherId: Int = 0,
    @SerializedName("hyperlink")
    val hyperlink: Hyperlink = Hyperlink(),
    @SerializedName("is_public")
    val isPublic: Int = 1,
    @SerializedName("is_quota_avaiable")
    val isQuotaAvaiable: Int = 0,
    @SerializedName("remaining_quota")
    val remainingQuota: Int = 0,
    @SerializedName("shop_id")
    val shopId: Int = 0,
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("update_by")
    val updateBy: Int = 0,
    @SerializedName("update_time")
    val updateTime: String = "",
    @SerializedName("voucher_code")
    val voucherCode: String = "",
    @SerializedName("voucher_discount_amt")
    val voucherDiscountAmt: Int = 0,
    @SerializedName("voucher_discount_amt_formatted")
    val voucherDiscountAmtFormatted: String = "",
    @SerializedName("voucher_discount_amt_max")
    val voucherDiscountAmtMax: Int = 0,
    @SerializedName("voucher_discount_amt_max_formatted")
    val voucherDiscountAmtMaxFormatted: String = "",
    @SerializedName("voucher_discount_type")
    val voucherDiscountType: Int = 0,
    @SerializedName("voucher_discount_type_formatted")
    val voucherDiscountTypeFormatted: String = "",
    @SerializedName("voucher_finish_time")
    val voucherFinishTime: String = "",
    @SerializedName("voucher_id")
    val voucherId: Int = 0,
    @SerializedName("voucher_image")
    val voucherImage: String = "",
    @SerializedName("voucher_image_square")
    val voucherImageSquare: String = "",
    @SerializedName("voucher_minimum_amt")
    val voucherMinimumAmt: Int = 0,
    @SerializedName("voucher_minimum_amt_formatted")
    val voucherMinimumAmtFormatted: String = "",
    @SerializedName("voucher_name")
    val voucherName: String = "",
    @SerializedName("voucher_quota")
    val voucherQuota: Int = 0,
    @SerializedName("voucher_start_time")
    val voucherStartTime: String = "",
    @SerializedName("voucher_status")
    val voucherStatus: Int = 0,
    @SerializedName("voucher_status_formatted")
    val voucherStatusFormatted: String = "",
    @SerializedName("voucher_type")
    val voucherType: Int = 0,
    @SerializedName("voucher_type_formatted")
    val voucherTypeFormatted: String = "",
    @SerializedName("is_lock_to_product")
    val isLockToProduct: Int = 0
)