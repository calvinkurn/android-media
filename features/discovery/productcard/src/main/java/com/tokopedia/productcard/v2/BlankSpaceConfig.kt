package com.tokopedia.productcard.v2

/**
 * Set true to set the component to mandatory
 * [When a component have true value]
 * That component will always reserve place even if the value of that component is empty
 */
class BlankSpaceConfig (
    val shopName: Boolean = false,
    val productName: Boolean = false,
    val discountPercentage: Boolean = false,
    val slashedPrice: Boolean = false,
    val price: Boolean = false,
    val shopLocation: Boolean = false,
    val ratingCount: Boolean = false,
    val reviewCount: Boolean = false,
    val labelCredibility: Boolean = false,
    val freeOngkir: Boolean = false,
    val labelOffers: Boolean = false,
    val twoLinesProductName: Boolean = false
)