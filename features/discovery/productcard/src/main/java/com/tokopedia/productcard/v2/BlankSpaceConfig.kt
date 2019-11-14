package com.tokopedia.productcard.v2

/**
 * Set true to set the component to mandatory
 * [When a component have true value]
 * That component will always reserve place even if the value of that component is empty
 */
data class BlankSpaceConfig (
    var shopName: Boolean = false,
    var productName: Boolean = false,
    var discountPercentage: Boolean = false,
    var slashedPrice: Boolean = false,
    var price: Boolean = false,
    var shopBadge: Boolean = false,
    var shopLocation: Boolean = false,
    var ratingCount: Boolean = false,
    var reviewCount: Boolean = false,
    var labelCredibility: Boolean = false,
    var freeOngkir: Boolean = false,
    var labelOffers: Boolean = false,
    var twoLinesProductName: Boolean = false
)