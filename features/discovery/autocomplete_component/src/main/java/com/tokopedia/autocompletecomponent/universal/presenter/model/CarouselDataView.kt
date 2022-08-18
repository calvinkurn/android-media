package com.tokopedia.autocompletecomponent.universal.presenter.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel

class CarouselDataView(
    val id: String = "",
    val applink: String = "",
    val title: String = "",
    val subtitle: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val product: List<Product> = listOf(),
) {
    class Product(
        val id: String = "",
        val url: String = "",
        val applink: String = "",
        val imageUrl: String = "",
        val title: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val price: String = "",
        val originalPrice: String = "",
        val discountPercentage: String = "",
        val ratingAverage: String = "",
        val countSold: String = "",
        val shop: Shop = Shop(),
        val badge: Badge = Badge(),
    )

    class Shop(
        val name: String = "",
        val city: String = "",
        val url: String = "",
    )

    class Badge(
        val title: String = "",
        val imageUrl: String = "",
        val show: Boolean = false,
    )
}