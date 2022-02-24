package com.tokopedia.vouchercreation.product.create.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import kotlinx.android.parcel.Parcelize

data class CouponDetailResponse(
        @SerializedName("merchantPromotionGetMVDataByID")
        val result: CouponDetailResult = CouponDetailResult()
)

data class CouponDetailResult(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val voucher: Voucher = Voucher()
)

data class Voucher(
        @Expose
        @SerializedName("confirmed_global_quota")
        val confirmedQuota: Int = 0,
        @Expose
        @SerializedName("booked_global_quota")
        val bookedQuota: Int = 0,
        @Expose
        @SerializedName("create_time")
        val createTime: String = "",
        @Expose
        @SerializedName("is_public")
        val isPublic: Int = 0,
        @Expose
        @SerializedName("remaining_quota")
        val remainingQuota: Int = 0,
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
        @SerializedName("voucher_discount_type_formatted")
        val discountTypeFormatted: String = "",
        @SerializedName("voucher_discount_type")
        val discountType: Int = 0,
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
        @SerializedName("voucher_image_portrait")
        val imagePortrait: String = "",
        @Expose
        @SerializedName("voucher_minimum_amt")
        val voucherMinimumAmt: Int = 0,
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
        @SerializedName("voucher_type")
        val voucherType: Int = 0,
        @Expose
        @SerializedName("voucher_type_formatted")
        val voucherTypeFormatted: String = "",
        @Expose
        @SerializedName("is_vps")
        val isVps: Int = 0,
        @Expose
        @SerializedName("package_name")
        val packageName: String = "",
        @Expose
        @SerializedName("is_subsidy")
        val isSubsidy: Int = 0,
        @Expose
        @SerializedName("tnc")
        val tnc: String = "",
        @Expose
        @SerializedName("product_ids")
        val productIds: List<ProductId> = emptyList(),
        @Expose
        @SerializedName("galadriel_voucher_id")
        val galadrielVoucherId: Long = 0,
)

@Parcelize
data class ProductId(
        @Expose
        @SerializedName("parent_product_id")
        val parentProductId: Long = 0,
        @Expose
        @SerializedName("child_product_id")
        val childProductId: List<Long> = emptyList()
) : Parcelable
