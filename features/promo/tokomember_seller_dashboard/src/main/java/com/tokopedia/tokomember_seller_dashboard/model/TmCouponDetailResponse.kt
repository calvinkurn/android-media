package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmCouponDetailResponse(
	@SerializedName("data")
	val tmCouponDetailResponseData: TmCouponDetailResponseData? = null
)

data class TmCouponDetailResponseData(
	@SerializedName("merchantPromotionGetMVDataByID")
	val merchantPromotionGetMVDataByID: MerchantPromotionGetMVDataByID? = null
)

data class TmCouponDetailData(

	@SerializedName("target_buyer")
	val targetBuyer: Int? = null,

	@SerializedName("galadriel_catalog_id")
	val galadrielCatalogId: Int? = null,

	@SerializedName("minimum_tier_level")
	val minimumTierLevel: Int? = null,

	@SerializedName("vps_unique_id")
	val vpsUniqueId: Int? = null,

	@SerializedName("voucher_code")
	val voucherCode: String? = null,

	@SerializedName("is_vps")
	val isVps: Int? = null,

	@SerializedName("galadriel_voucher_id")
	val galadrielVoucherId: Int? = null,

	@SerializedName("voucher_package_id")
	val voucherPackageId: Int? = null,

	@SerializedName("is_subsidy")
	val isSubsidy: Int? = null,

	@SerializedName("voucher_start_time")
	val voucherStartTime: String? = null,

	@SerializedName("create_by")
	val createBy: Int? = null,

	@SerializedName("update_time")
	val updateTime: String? = null,

	@SerializedName("voucher_image")
	val voucherImage: String? = null,

	@SerializedName("voucher_discount_type_formatted")
	val voucherDiscountTypeFormatted: String? = null,

	@SerializedName("voucher_discount_amt_max")
	var voucherDiscountAmtMax: Int? = null,

	@SerializedName("voucher_image_portrait")
	var voucherImagePortrait: String? = null,

	@SerializedName("voucher_status_formatted")
	val voucherStatusFormatted: String? = null,

	@SerializedName("booked_global_quota")
	val bookedGlobalQuota: Int? = null,

	@SerializedName("update_by")
	val updateBy: Int? = null,

	@SerializedName("voucher_minimum_amt")
	val voucherMinimumAmt: Int? = null,

	@SerializedName("confirmed_global_quota")
	val confirmedGlobalQuota: Int? = null,

	@SerializedName("applink")
	val applink: String? = null,

	@SerializedName("create_time")
	val createTime: String? = null,

	@SerializedName("voucher_name")
	val voucherName: String? = null,

	@SerializedName("voucher_status")
	val voucherStatus: Int? = null,

	@SerializedName("is_quota_avaiable")
	val isQuotaAvaiable: Int? = null,

	@SerializedName("voucher_discount_amt")
	val voucherDiscountAmt: Int? = null,

	@SerializedName("voucher_minimum_amt_formatted")
	val voucherMinimumAmtFormatted: String? = null,

	@SerializedName("tnc")
	val tnc: String? = null,

	@SerializedName("voucher_quota")
	val voucherQuota: Int? = null,

	@SerializedName("voucher_image_square")
	var voucherImageSquare: String? = null,

	@SerializedName("vps_bundling_id")
	val vpsBundlingId: Int? = null,

	@SerializedName("voucher_discount_amt_max_formatted")
	val voucherDiscountAmtMaxFormatted: String? = null,

	@SerializedName("shop_id")
	val shopId: Int? = null,

	@SerializedName("is_lock_to_product")
	val isLockToProduct: Int? = null,

	@SerializedName("product_ids")
	val productIds: List<Any?>? = null,

	@SerializedName("voucher_type")
	val voucherType: Int? = null,

	@SerializedName("voucher_discount_amt_formatted")
	val voucherDiscountAmtFormatted: String? = null,

	@SerializedName("is_public")
	val isPublic: Int? = null,

	@SerializedName("package_name")
	val packageName: String? = null,

	@SerializedName("weblink")
	val weblink: String? = null,

	@SerializedName("voucher_id")
	val voucherId: Int? = null,

	@SerializedName("voucher_discount_type")
	val voucherDiscountType: Int? = null,

	@SerializedName("remaning_quota")
	val remaningQuota: Int? = null,

	@SerializedName("voucher_finish_time")
	val voucherFinishTime: String? = null,

	@SerializedName("voucher_type_formatted")
	val voucherTypeFormatted: String? = null,

	@SerializedName("warehouse_id")
	val warehouseId: Int? = null
)

data class MerchantPromotionGetMVDataByID(

	@SerializedName("data")
	val data: TmCouponDetailData? = null,

	@SerializedName("header")
	val header: Header? = null
)
