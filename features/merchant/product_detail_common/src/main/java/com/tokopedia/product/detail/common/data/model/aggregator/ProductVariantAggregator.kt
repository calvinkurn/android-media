package com.tokopedia.product.detail.common.data.model.aggregator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.common.data.model.product.Cashback
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.usp.UniqueSellingPointTokoCabang
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.NearestWarehouseResponse

/**
 * Created by Yehezkiel on 05/05/21
 */

data class ProductVariantAggregatorResponse(
        @SerializedName("pdpGetVariantComponent")
        @Expose
        val response: ProductVariantAggregator = ProductVariantAggregator()
)

data class ProductVariantAggregator(
        @SerializedName("variantData")
        @Expose
        val variantData: ProductVariant = ProductVariant(),

        @SerializedName("cartRedirection")
        @Expose
        val cardRedirection: CartRedirection = CartRedirection(),

        @SerializedName("warehouseInfo")
        @Expose
        val nearestWarehouse: List<NearestWarehouseResponse> = listOf(),

        @SerializedName("basicInfo")
        @Expose
        val basicInfo: SimpleBasicInfo = SimpleBasicInfo(),

        @SerializedName("shopInfo")
        @Expose
        val shopInfo: SimpleShopInfo = SimpleShopInfo(),

        @SerializedName("bebasOngkir")
        @Expose
        var bebasOngkir: BebasOngkir = BebasOngkir(),

        @SerializedName("ratesEstimate")
        @Expose
        var ratesEstimate: List<P2RatesEstimate> = listOf(),

        @SerializedName("restrictionInfo")
        @Expose
        var restrictionInfo: RestrictionInfoResponse = RestrictionInfoResponse(),

        @SerializedName("uniqueSellingPoint")
        @Expose
        var uniqueSellingPoint: UniqueSellingPointTokoCabang = UniqueSellingPointTokoCabang(),

        @SerializedName("isCashback")
        @Expose
        var isCashback: Cashback = Cashback(),

        @SerializedName("isCOD")
        @Expose
        var isCod: Boolean = false

)