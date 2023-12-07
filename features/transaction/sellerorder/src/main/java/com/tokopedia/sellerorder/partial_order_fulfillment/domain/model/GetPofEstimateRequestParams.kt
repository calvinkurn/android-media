package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

data class GetPofEstimateRequestParams(
    @SerializedName("detail_info")
    @Expose
    val detailInfo: List<DetailInfo?> = emptyList(),
    @SerializedName("order_id")
    @Expose
    val orderId: Long = 0,
    @SerializedName("delay")
    @Expose(serialize = false, deserialize = false)
    @Transient
    val delay: Long = 0,
    @SerializedName("cache_type")
    @Expose(serialize = false, deserialize = false)
    @Transient
    val cacheType: CacheType = CacheType.ALWAYS_CLOUD
) {
    fun valid(): Boolean {
        return detailInfo.filterNotNull().isNotEmpty() && orderId.isMoreThanZero()
    }

    data class DetailInfo(
        @SerializedName("order_dtl_id")
        @Expose
        val orderDetailId: Long? = null,
        @SerializedName("product_id")
        @Expose
        val productId: Long? = null,
        @SerializedName("quantity_request")
        @Expose
        val quantityRequest: Int? = null
    )
}
