package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmCouponListResponse(

//	@SerializedName("data")
//	val data: TmCouponListData? = null,

	@SerializedName("MerchantPromotionGetMVList")
	val merchantPromotionGetMVList: MerchantPromotionGetMVList? = null,

)
data class Paging(

	@SerializedName("per_page")
	val perPage: Int? = null,

	@SerializedName("has_next")
	val hasNext: Boolean? = null,

	@SerializedName("page")
	val page: Int? = null,

	@SerializedName("has_prev")
	val hasPrev: Boolean? = null
)

data class Hyperlink(

	@SerializedName("stop")
	val stop: String? = null,

	@SerializedName("edit")
	val edit: String? = null,

	@SerializedName("edit_quota_ajax")
	val editQuotaAjax: String? = null,

	@SerializedName("share")
	val share: String? = null,

	@SerializedName("delete")
	val delete: String? = null
)

data class VouchersItem(

	@SerializedName("minimum_tier_level")
	val minimumTierLevel: Int? = null,

	@SerializedName("vps_unique_id")
	val vpsUniqueId: String? = null,

	@SerializedName("voucher_code")
	val voucherCode: String? = null,

	@SerializedName("galadriel_voucher_id")
	val galadrielVoucherId: String? = null,

	@SerializedName("voucher_package_id")
	val voucherPackageId: String? = null,

	@SerializedName("is_subsidy")
	val isSubsidy: Int? = null,

	@SerializedName("voucher_image")
	val voucherImage: String? = null,

	@SerializedName("booked_global_quota")
	val bookedGlobalQuota: Int? = null,

	@SerializedName("hyperlink")
	val hyperlink: Hyperlink? = null,

	@SerializedName("applink")
	val applink: String? = null,

	@SerializedName("create_time")
	val createTime: String? = null,

	@SerializedName("performance_income")
	val performanceIncome: Int? = null,

	@SerializedName("voucher_name")
	val voucherName: String? = null,

	@SerializedName("voucher_status")
	val voucherStatus: Int? = null,

	@SerializedName("is_quota_avaiable")
	val isQuotaAvaiable: Int? = null,

	@SerializedName("voucher_minimum_amt_formatted")
	val voucherMinimumAmtFormatted: String? = null,

	@SerializedName("tnc")
	val tnc: String? = null,

	@SerializedName("voucher_quota")
	val voucherQuota: Int? = null,

	@SerializedName("vps_bundling_id")
	val vpsBundlingId: String? = null,

	@SerializedName("voucher_discount_amt_max_formatted")
	val voucherDiscountAmtMaxFormatted: String? = null,

	@SerializedName("shop_id")
	val shopId: String? = null,

	@SerializedName("voucher_discount_amt_formatted")
	val voucherDiscountAmtFormatted: String? = null,

	@SerializedName("weblink")
	val weblink: String? = null,

	@SerializedName("performance_income_formatted")
	val performanceIncomeFormatted: String? = null,

	@SerializedName("voucher_discount_type")
	val voucherDiscountType: Int? = null,

	@SerializedName("total_new_follower")
	val totalNewFollower: Int? = null,

	@SerializedName("voucher_type_formatted")
	val voucherTypeFormatted: String? = null,

	@SerializedName("target_buyer")
	val targetBuyer: Int? = null,

	@SerializedName("galadriel_catalog_id")
	val galadrielCatalogId: String? = null,

	@SerializedName("remaining_quota")
	val remainingQuota: Int? = null,

	@SerializedName("is_vps")
	val isVps: Int? = null,

	@SerializedName("voucher_start_time")
	val voucherStartTime: String? = null,

	@SerializedName("create_by")
	val createBy: Int? = null,

	@SerializedName("update_time")
	val updateTime: String? = null,

	@SerializedName("voucher_discount_type_formatted")
	val voucherDiscountTypeFormatted: String? = null,

	@SerializedName("voucher_discount_amt_max")
	val voucherDiscountAmtMax: Int? = null,

	@SerializedName("voucher_image_portrait")
	val voucherImagePortrait: String? = null,

	@SerializedName("voucher_status_formatted")
	val voucherStatusFormatted: String? = null,

	@SerializedName("update_by")
	val updateBy: Int? = null,

	@SerializedName("voucher_minimum_amt")
	val voucherMinimumAmt: Int? = null,

	@SerializedName("performance_outcome")
	val performanceOutcome: Int? = null,

	@SerializedName("performance_outcome_formatted")
	val performanceOutcomeFormatted: String? = null,

	@SerializedName("used_global_quota")
	val usedGlobalQuota: Int? = null,

	@SerializedName("voucher_discount_amt")
	val voucherDiscountAmt: Int? = null,

	@SerializedName("voucher_image_square")
	val voucherImageSquare: String? = null,

	@SerializedName("is_lock_to_product")
	val isLockToProduct: Int? = null,

	@SerializedName("voucher_type")
	val voucherType: Int? = null,

	@SerializedName("is_public")
	val isPublic: Int? = null,

	@SerializedName("package_name")
	val packageName: String? = null,

	@SerializedName("voucher_id")
	val voucherId: String? = null,

	@SerializedName("voucher_finish_time")
	val voucherFinishTime: String? = null,

	@SerializedName("warehouse_id")
	val warehouseId: String? = null
)

data class TmCouponListData(

	@SerializedName("paging")
	val paging: Paging? = null,

	@SerializedName("vouchers")
	val vouchers: List<VouchersItem?>? = null
)

data class MerchantPromotionGetMVList(

	@SerializedName("data")
	val data: TmCouponListData? = null,

	@SerializedName("header")
	val header: Header? = null
)

data class Header(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("error_code")
	val errorCode: String? = null,

	@SerializedName("message")
	val message: List<Any?>? = null,

	@SerializedName("process_time")
	val processTime: Double? = null
)
