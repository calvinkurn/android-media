package com.tokopedia.shop.home.view.customview.directpurchase

data class WidgetData(
    val widgetTitle: String,
    val titleList: List<Title>?
)

data class ProductCardDirectPurchaseDataModel(
    val productId: String = "",
    val imageUrl: String = "",
    val name: String = "",
    // listOf("ff0000", ..."). If it has no color variant, this will be empty
    val colorVariant: List<String> = emptyList(),
    val price: String = "", // eg Rp80.000
    val discount: String = "", // eg 20%
    val slashPrice: String = "", // eg Rp100.000
    val ratingAverage: String = "", // 4.9
    val label: String = "", // Terjual 3,3 rb
)

data class DirectPurchaseWidgetData(
    val titleList: List<Title>
)

data class Title(
    var title: String?,
    val imageUrl: String?,
    var etalaseList: List<Etalase>
)

data class Etalase(
    var etalaseId: String,
    var name: String,
    var imageUrl: String,
    var productList: List<ProductCardDirectPurchaseDataModel>,
    // time when the productList is retrieved from network.
    var lastTimeStampProductListCaptured: Long = 0L,
    // if product list fetch is error, put the error message to be shown here.
    var errorMessageIfFailedFetch: String? = null
)