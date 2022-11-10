package com.tokopedia.logisticcart.scheduledelivery.model

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
    val type: Int = TYPE_ANDROID,
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
) {

    fun toMap() = mapOf(
        "param" to requestParam()
    )

    private fun requestParam() = mapOf(
        "origin" to origin,
        "destination" to destination,
        "spids" to spids,
        "warehouse_id" to warehouseId,
        "cat_id" to catId,
        "products" to products,
        "bo_metadata" to boMetadata,
        "weight" to weight,
        "actual_weight" to actualWeight,
        "type" to type,
        "user_history" to userHistory,
        "unique_id" to uniqueId,
        "po_time" to poTime,
        "shop_id" to shopId,
        "is_fulfillment" to isFulfillment,
        "shop_tier" to shopTier,
        "time_slot_id" to timeSlotId,
        "schedule_date" to scheduleDate,
        "source" to source
    )

    companion object {
        private const val TYPE_ANDROID = 1
        private const val SOURCE_CHECKOUT = "checkout"
    }
}
