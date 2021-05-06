package com.tokopedia.product.detail.common.data.model.aggregator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 05/05/21
 */
data class ProductVariantAggregator(
        @SerializedName("variantData")
        @Expose
        var variantData: VariantData = VariantData()
)

data class VariantData(
        @SerializedName("parentID")
        @Expose
        val parentId: String = "",
        @SerializedName("errorCode")
        @Expose
        val errorCode: Int = 0,
        @SerializedName("sizeChart")
        @Expose
        val sizeChart: String = "",
        @SerializedName("defaultChild")
        @Expose
        val defaultChild: String = "",
        @SerializedName("variants")
        @Expose
        val variants: List<Variant> = listOf(),
        @SerializedName("children")
        @Expose
        val children: List<VariantChild> = listOf(),

        @SerializedName("cartRedirection")
        @Expose
        val cardRedirection: CartRedirection = CartRedirection(),

        @SerializedName("warehouseInfo")
        @Expose
        val warehouseInfo: WarehouseInfo = WarehouseInfo()
)