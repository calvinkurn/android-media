package com.tokopedia.cart.data.model.response.cartshoptickeraggregator

import com.google.gson.annotations.SerializedName

data class CartShopGroupTickerAggregatorResponse(
    @SerializedName("data")
    val data: CartShopGroupTickerAggregatorData = CartShopGroupTickerAggregatorData(),
)

data class CartShopGroupTickerAggregatorData(
    @SerializedName("min_transaction")
    val minTransaction: Long = 0,
    @SerializedName("ticker")
    val ticker: CartShopGroupTickerAggregatorTicker = CartShopGroupTickerAggregatorTicker(),
    @SerializedName("bundle_bottomsheet")
    val bundleBottomSheet: CartShopGroupTickerAggregatorBundleBottomSheet = CartShopGroupTickerAggregatorBundleBottomSheet(),
    @SerializedName("aggregator_type")
    val aggregatorType: Int = 0,
)

data class CartShopGroupTickerAggregatorTicker(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("left_icon")
    val leftIcon: String = "",
    @SerializedName("right_icon")
    val rightIcon: String = "",
)

data class CartShopGroupTickerAggregatorBundleBottomSheet(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("bottom_ticker")
    val bottomTicker: String = "",
)
