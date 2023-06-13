package com.tokopedia.mvc.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class MerchantPromotionGetMVDataByIDResponse(
    @SerializedName("merchantPromotionGetMVDataByID")
    val merchantPromotionGetMVDataByID: MerchantPromotionGetMVDataByID = MerchantPromotionGetMVDataByID()
) {
    data class MerchantPromotionGetMVDataByID(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
    ) {

        data class Data(
            @SerializedName("voucher_id")
            val voucherId: Long = 0,
            @SuppressLint("Invalid Data Type")
            @SerializedName("shop_id")
            val shopId: Long = 0,
            @SerializedName("voucher_name")
            val voucherName: String = "",
            @SerializedName("voucher_type")
            val voucherType: Int = 0,
            @SerializedName("voucher_image")
            val voucherImage: String = "",
            @SerializedName("voucher_image_square")
            val voucherImageSquare: String = "",
            @SerializedName("voucher_image_portrait")
            val voucherImagePortrait: String = "",
            @SerializedName("voucher_status")
            val voucherStatus: Int = 0,
            @SerializedName("voucher_discount_type")
            val voucherDiscountType: Int = 0,
            @SerializedName("voucher_discount_amt")
            val voucherDiscountAmount: Long = 0,
            @SerializedName("voucher_discount_amt_max")
            val voucherDiscountAmountMax: Long = 0,
            @SerializedName("voucher_minimum_amt")
            val voucherDiscountAmountMin: Long = 0,
            @SerializedName("voucher_quota")
            val voucherQuota: Long = 0,
            @SerializedName("voucher_start_time")
            val voucherStartTime: String = "",
            @SerializedName("voucher_finish_time")
            val voucherFinishTime: String = "",
            @SerializedName("voucher_code")
            val voucherCode: String = "",
            @SerializedName("galadriel_voucher_id")
            val galadrielVoucherId: Long = 0,
            @SerializedName("galadriel_catalog_id")
            val galadrielCatalogId: Long = 0,
            @SerializedName("create_time")
            val createTime: String = "",
            @SerializedName("create_by")
            val createBy: Long = 0,
            @SerializedName("update_time")
            val updateTime: String = "",
            @SerializedName("update_by")
            val updateBy: Int = 0,
            @SerializedName("is_public")
            val isPublic: Int = 0,
            @SerializedName("is_child")
            val isChild: Int = 0,
            @SerializedName("is_quota_avaiable")
            val isQuotaAvailable: Int = 0,
            @SerializedName("voucher_type_formatted")
            val voucherTypeFormatted: String = "",
            @SerializedName("voucher_status_formatted")
            val voucherStatusFormatted: String = "",
            @SerializedName("voucher_discount_type_formatted")
            val voucherDiscountTypeFormatted: String = "",
            @SerializedName("voucher_discount_amt_formatted")
            val voucherDiscountAmountFormatted: String = "",
            @SerializedName("voucher_discount_amt_max_formatted")
            val voucherDiscountAmountMaxFormatted: String = "",
            @SerializedName("remaning_quota")
            val remainingQuota: Long = 0,
            @SerializedName("tnc")
            val tnc: String = "",
            @SerializedName("booked_global_quota")
            val bookedGlobalQuota: Long = 0,
            @SerializedName("confirmed_global_quota")
            val confirmedGlobalQuota: Long = 0,
            @SerializedName("target_buyer")
            val targetBuyer: Int = 0,
            @SerializedName("minimum_tier_level")
            val minimumTierLevel: Int = 0,
            @SerializedName("is_lock_to_product")
            val isLockToProduct: Int = 0,
            @SerializedName("is_vps")
            val isVps: Int = 0,
            @SerializedName("package_name")
            val packageName: String = "",
            @SerializedName("vps_unique_id")
            val vpsUniqueId: Long = 0,
            @SerializedName("voucher_package_id")
            val voucherPackageId: Long = 0,
            @SerializedName("vps_bundling_id")
            val vpsBundlingId: Long = 0,
            @SerializedName("is_subsidy")
            val isSubsidy: Int = 0,
            @SerializedName("applink")
            val appLink: String = "",
            @SerializedName("weblink")
            val webLink: String = "",
            @SerializedName("warehouse_id")
            val warehouseId: Long = 0,
            @SerializedName("voucher_minimum_amt_type")
            val voucherMinimumAmountType: Int = 0,
            @SerializedName("voucher_minimum_amt_type_formatted")
            val voucherMinimumAmountTypeFormatted: String = "",
            @SerializedName("is_period")
            val isPeriod: Boolean = false,
            @SerializedName("total_period")
            val totalPeriod: Int = 0,
            @SerializedName("voucher_lock_type")
            val voucherLockType: String = "",
            @SerializedName("voucher_lock_id")
            val voucherLockId: Long = 0,
            @SerializedName("product_ids")
            val productIds: List<ProductId> = listOf()
        ) {
            data class ProductId(
                @SerializedName("parent_product_id")
                val parentProductId: Long = 0,
                @SuppressLint("Invalid Data Type")
                @SerializedName("child_product_id")
                val chilProductId: List<Long>? = listOf()
            )
        }

        data class Header(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("messages")
            val messages: List<String>? = listOf(),
            @SerializedName("reason")
            val reason: String = ""
        )
    }
}
