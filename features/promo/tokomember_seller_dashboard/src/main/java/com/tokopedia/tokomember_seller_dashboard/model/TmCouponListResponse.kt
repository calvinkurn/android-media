package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmCouponListResponse(

//	@Expose
//	@SerializedName("data")
//	val data: TmCouponListData? = null,

	@Expose
	@SerializedName("MerchantPromotionGetMVList")
	val merchantPromotionGetMVList: MerchantPromotionGetMVList? = null,

)
data class Paging(

	@Expose
	@SerializedName("per_page")
	val perPage: Int = 0,

	@Expose
	@SerializedName("has_next")
	val hasNext: Boolean = false,

	@Expose
	@SerializedName("page")
	val page: Int = 0,

	@Expose
	@SerializedName("has_prev")
	val hasPrev: Boolean = false
)

data class Hyperlink(

	@Expose
	@SerializedName("stop")
	val stop: String = "",

	@Expose
	@SerializedName("edit")
	val edit: String = "",

	@Expose
	@SerializedName("edit_quota_ajax")
	val editQuotaAjax: String = "",

	@Expose
	@SerializedName("share")
	val share: String = "",

	@Expose
	@SerializedName("delete")
	val delete: String = ""
)

data class VouchersItem(

	@Expose
	@SerializedName("minimum_tier_level")
	val minimumTierLevel: Int = 0,

	@Expose
	@SerializedName("vps_unique_id")
	val vpsUniqueId: String = "",

	@Expose
	@SerializedName("voucher_code")
	val voucherCode: String = "",

	@Expose
	@SerializedName("galadriel_voucher_id")
	val galadrielVoucherId: String = "",

	@Expose
	@SerializedName("voucher_package_id")
	val voucherPackageId: String = "",

	@Expose
	@SerializedName("is_subsidy")
	val isSubsidy: Int = 0,

	@Expose
	@SerializedName("voucher_image")
	val voucherImage: String = "",

	@Expose
	@SerializedName("booked_global_quota")
	val bookedGlobalQuota: Int = 0,

	@Expose
	@SerializedName("hyperlink")
	val hyperlink: Hyperlink? = null,

	@Expose
	@SerializedName("applink")
	val applink: String = "",

	@Expose
	@SerializedName("create_time")
	val createTime: String = "",

	@Expose
	@SerializedName("performance_income")
	val performanceIncome: Int = 0,

	@Expose
	@SerializedName("voucher_name")
	val voucherName: String = "",

	@Expose
	@SerializedName("voucher_status")
	val voucherStatus: Int = 0,

	@Expose
	@SerializedName("is_quota_avaiable")
	val isQuotaAvaiable: Int = 0,

	@Expose
	@SerializedName("voucher_minimum_amt_formatted")
	val voucherMinimumAmtFormatted: String = "",

	@Expose
	@SerializedName("tnc")
	val tnc: String = "",

	@Expose
	@SerializedName("voucher_quota")
	val voucherQuota: Int = 0,

	@Expose
	@SerializedName("vps_bundling_id")
	val vpsBundlingId: String = "",

	@Expose
	@SerializedName("voucher_discount_amt_max_formatted")
	val voucherDiscountAmtMaxFormatted: String = "",

	@Expose
	@SerializedName("shop_id")
	val shopId: String = "",

	@Expose
	@SerializedName("voucher_discount_amt_formatted")
	val voucherDiscountAmtFormatted: String = "",

	@Expose
	@SerializedName("weblink")
	val weblink: String = "",

	@Expose
	@SerializedName("performance_income_formatted")
	val performanceIncomeFormatted: String = "",

	@Expose
	@SerializedName("voucher_discount_type")
	val voucherDiscountType: Int = 0,

	@Expose
	@SerializedName("total_new_follower")
	val totalNewFollower: Int = 0,

	@Expose
	@SerializedName("voucher_type_formatted")
	val voucherTypeFormatted: String = "",

	@Expose
	@SerializedName("target_buyer")
	val targetBuyer: Int = 0,

	@Expose
	@SerializedName("galadriel_catalog_id")
	val galadrielCatalogId: String = "",

	@Expose
	@SerializedName("remaining_quota")
	val remainingQuota: Int = 0,

	@Expose
	@SerializedName("is_vps")
	val isVps: Int = 0,

	@Expose
	@SerializedName("voucher_start_time")
	val voucherStartTime: String = "",

	@Expose
	@SerializedName("create_by")
	val createBy: Int = 0,

	@Expose
	@SerializedName("update_time")
	val updateTime: String = "",

	@Expose
	@SerializedName("voucher_discount_type_formatted")
	val voucherDiscountTypeFormatted: String = "",

	@Expose
	@SerializedName("voucher_discount_amt_max")
	val voucherDiscountAmtMax: Int = 0,

	@Expose
	@SerializedName("voucher_image_portrait")
	val voucherImagePortrait: String = "",

	@Expose
	@SerializedName("voucher_status_formatted")
	val voucherStatusFormatted: String = "",

	@Expose
	@SerializedName("update_by")
	val updateBy: Int = 0,

	@Expose
	@SerializedName("voucher_minimum_amt")
	val voucherMinimumAmt: Int = 0,

	@Expose
	@SerializedName("performance_outcome")
	val performanceOutcome: Int = 0,

	@Expose
	@SerializedName("performance_outcome_formatted")
	val performanceOutcomeFormatted: String = "",

	@Expose
	@SerializedName("used_global_quota")
	val usedGlobalQuota: Int = 0,

	@Expose
	@SerializedName("voucher_discount_amt")
	val voucherDiscountAmt: Int = 0,

	@Expose
	@SerializedName("voucher_image_square")
	val voucherImageSquare: String = "",

	@Expose
	@SerializedName("is_lock_to_product")
	val isLockToProduct: Int = 0,

	@Expose
	@SerializedName("voucher_type")
	val voucherType: Int = 0,

	@Expose
	@SerializedName("is_public")
	val isPublic: Int = 0,

	@Expose
	@SerializedName("package_name")
	val packageName: String = "",

	@Expose
	@SerializedName("voucher_id")
	val voucherId: String = "",

	@Expose
	@SerializedName("voucher_finish_time")
	val voucherFinishTime: String = "",

	@Expose
	@SerializedName("warehouse_id")
	val warehouseId: String = ""
)

data class TmCouponListData(

	@Expose
	@SerializedName("paging")
	val paging: Paging? = null,

	@Expose
	@SerializedName("vouchers")
	val vouchers: List<VouchersItem?>? = null
)

data class MerchantPromotionGetMVList(

	@Expose
	@SerializedName("data")
	val data: TmCouponListData? = null,

	@Expose
	@SerializedName("header")
	val header: Header? = null
)

data class Header(

	@Expose
	@SerializedName("reason")
	val reason: String = "",

	@Expose
	@SerializedName("error_code")
	val errorCode: String = "",

	@Expose
	@SerializedName("message")
	val message: List<Any?>? = null,

	@Expose
	@SerializedName("process_time")
	val processTime: Double? = null
)

data class CouponItem(
    var coupon: VouchersItem,
    var layout: LayoutType = LayoutType.SHOW_CARD
)

enum class LayoutType{
    SHOW_CARD, LOADER
}
