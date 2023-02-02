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
)

data class CartShopGroupTickerAggregatorTicker(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("icon")
    val icon: CartShopGroupTickerAggregatorTickerIcon = CartShopGroupTickerAggregatorTickerIcon(),
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("action")
    val action: String = "",
)

data class CartShopGroupTickerAggregatorTickerIcon(
    @SerializedName("left_icon")
    val leftIcon: String = "",
    @SerializedName("left_icon_dark")
    val leftIconDark: String = "",
    @SerializedName("right_icon")
    val rightIcon: String = "",
    @SerializedName("right_icon_dark")
    val rightIconDark: String = "",
)

data class CartShopGroupTickerAggregatorBundleBottomSheet(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("bottom_ticker")
    val bottomTicker: String = "",
)
