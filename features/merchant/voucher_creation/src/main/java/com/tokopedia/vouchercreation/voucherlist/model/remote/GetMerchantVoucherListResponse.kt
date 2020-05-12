package com.tokopedia.vouchercreation.voucherlist.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 11/05/20
 */

data class GetMerchantVoucherListResponse(
        @Expose
        @SerializedName("MerchantPromotionGetMVList")
        val result: MerchantVoucherDataModel = MerchantVoucherDataModel()
)

data class MerchantVoucherDataModel(
        @Expose
        @SerializedName("data")
        val data: MerchantVoucherListDataModel = MerchantVoucherListDataModel()
)

data class MerchantVoucherListDataModel(
        @Expose
        @SerializedName("vouchers")
        val vouchers: List<MerchantVoucherModel> = emptyList(),
        @Expose
        @SerializedName("paging")
        val paging: PagingModel = PagingModel()
)

data class MerchantVoucherModel(
        @Expose
        @SerializedName("booked_global_quota")
        val bookedQuota: Int = 0,
        @Expose
        @SerializedName("create_by")
        val createBy: Int = 0,
        @Expose
        @SerializedName("create_time")
        val createTime: String = "",
        @Expose
        @SerializedName("galadriel_catalog_id")
        val galadrielCatalogId: Int = 0,
        @Expose
        @SerializedName("galadriel_voucher_id")
        val galadrielVoucherId: Int = 0,
        @Expose
        @SerializedName("is_public")
        val isPublic: Int = 0,
        @Expose
        @SerializedName("is_quota_avaiable")
        val isQuotaAvailable: Int = 0,
        @Expose
        @SerializedName("remaining_quota")
        val remainingQuota: Int = 0,
        @Expose
        @SerializedName("shop_id")
        val shopId: Int = 0,
        @Expose
        @SerializedName("tnc")
        val tnc: String = "",
        @Expose
        @SerializedName("update_by")
        val updateBy: Int = 0,
        @Expose
        @SerializedName("update_time")
        val updateTime: String = "",
        @Expose
        @SerializedName("voucher_code")
        val voucherCode: String = "",
        @Expose
        @SerializedName("voucher_discount_amt")
        val discountAmt: Int = 0,
        @Expose
        @SerializedName("voucher_discount_amt_formatted")
        val discountAmtFormatted: String = "",
        @Expose
        @SerializedName("voucher_discount_amt_max")
        val discountAmtMax: Int = 0,
        @Expose
        @SerializedName("voucher_discount_amt_max_formatted")
        val discountAmtMaxFormatted: String = "",
        @Expose
        @SerializedName("voucher_discount_type")
        val discountType: Int = 0,
        @Expose
        @SerializedName("voucher_discount_type_formatted")
        val discountTypeFormatted: String = "",
        @Expose
        @SerializedName("voucher_finish_time")
        val finishTime: String = "",
        @Expose
        @SerializedName("voucher_id")
        val voucherId: Int = 0,
        @Expose
        @SerializedName("voucher_image")
        val voucherImage: String = "",
        @Expose
        @SerializedName("voucher_image_square")
        val imageSquare: String = "",
        @Expose
        @SerializedName("voucher_minimum_amt")
        val voucherMinimumAmt: Int = 0,
        @Expose
        @SerializedName("voucher_minimum_amt_formatted")
        val voucherMinimumAmtFormatted: String = "",
        @Expose
        @SerializedName("voucher_name")
        val voucherName: String = "",
        @Expose
        @SerializedName("voucher_quota")
        val voucherQuota: Int = 0,
        @Expose
        @SerializedName("voucher_start_time")
        val startTime: String = "",
        @Expose
        @SerializedName("voucher_status")
        val voucherStatus: Int = 0,
        @Expose
        @SerializedName("voucher_status_formatted")
        val voucherStatusFormatted: String = "",
        @Expose
        @SerializedName("voucher_type")
        val voucherType: Int = 0,
        @Expose
        @SerializedName("Voucher_type_formatted")
        val voucherTypeFormatted: String = ""
)