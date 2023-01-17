package com.tokopedia.mvc.domain.entity


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdateCouponRequestParams(
    @SuppressLint("Invalid Data Type") // GQL still using number type
    @SerializedName("voucher_id")
    @Expose
    val voucherId: Long = 0,
    @SerializedName("benefit_idr")
    @Expose
    val benefitIdr: Long = 0,
    @SerializedName("benefit_max")
    @Expose
    val benefitMax: Long = 0,
    @SerializedName("benefit_percent")
    @Expose
    val benefitPercent: Int = 0,
    @SerializedName("benefit_type")
    @Expose
    val benefitType: String = "",
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("coupon_name")
    @Expose
    val couponName: String = "",
    @SerializedName("coupon_type")
    @Expose
    val couponType: String = "",
    @SerializedName("date_start")
    @Expose
    val dateStart: String = "",
    @SerializedName("date_end")
    @Expose
    val dateEnd: String = "",
    @SerializedName("hour_start")
    @Expose
    val hourStart: String = "",
    @SerializedName("hour_end")
    @Expose
    val hourEnd: String = "",
    @SerializedName("image")
    @Expose
    var image: String = "",
    @SerializedName("image_square")
    @Expose
    var imageSquare: String = "",
    @SerializedName("image_portrait")
    @Expose
    var imagePortrait: String = "",
    @SerializedName("is_public")
    @Expose
    val isPublic: Int = 0,
    @SerializedName("min_purchase")
    @Expose
    val minPurchase: Long = 0,
    @SerializedName("quota")
    @Expose
    val quota: Long = 0,
    @SerializedName("token")
    @Expose
    val token: String = "",
    @SerializedName("source")
    @Expose
    val source: String = "",
    @SerializedName("target_buyer")
    @Expose
    val targetBuyer: Int = 0,
    @SerializedName("minimum_tier_level")
    @Expose
    val minimumTierLevel: Int = 0,
    @SerializedName("is_lock_to_product")
    @Expose
    val isLockToProduct: Int = 0,
    @SerializedName("product_ids")
    @Expose
    val productIds: String = "",
    @SerializedName("product_ids_csv_url")
    @Expose
    val productIdsCsvUrl: String = "",
    @SuppressLint("Invalid Data Type") // GQL still using number type
    @SerializedName("warehouse_id")
    @Expose
    val warehouseId: Long = 0
)
