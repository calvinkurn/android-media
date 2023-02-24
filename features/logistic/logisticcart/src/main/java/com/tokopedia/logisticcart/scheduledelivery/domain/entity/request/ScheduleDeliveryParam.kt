package com.tokopedia.logisticcart.scheduledelivery.domain.entity.request

import com.google.gson.annotations.SerializedName

data class ScheduleDeliveryParam(
    @SerializedName("origin")
    val origin: String = "",
    @SerializedName("destination")
    val destination: String = "",
    @SerializedName("spids")
    val spids: String = "",
    @SerializedName("warehouse_id")
    val warehouseId: String = "",
    @SerializedName("cat_id")
    val catId: String = "",
    @SerializedName("products")
    val products: String = "",
    @SerializedName("bo_metadata")
    val boMetadata: String = "",
    @SerializedName("weight")
    val weight: String = "",
    @SerializedName("actual_weight")
    val actualWeight: String = "",
    @SerializedName("type")
    val type: String = TYPE_ANDROID,
    @SerializedName("user_history")
    val userHistory: Int = -1,
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("po_time")
    val poTime: Int = 0,
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("is_fulfillment")
    val isFulfillment: Boolean = false,
    @SerializedName("shop_tier")
    val shopTier: Int = 0,
    @SerializedName("time_slot_id")
    val timeSlotId: Long? = null,
    @SerializedName("schedule_date")
    val scheduleDate: String? = null,
    @SerializedName("source")
    val source: String = SOURCE_CHECKOUT,
    @SerializedName("order_value")
    val orderValue: Long = 0,
    @SerializedName("cart_data")
    val cartData: String = ""
) {

    fun toMap() = mapOf(
        "param" to this
    )

    companion object {
        private const val TYPE_ANDROID = "1"
        private const val SOURCE_CHECKOUT = "checkout"
    }
}
