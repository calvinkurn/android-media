package com.tokopedia.productcard.v2

/**
 * Set true to set the component to mandatory
 * [When a component have true value]
 * That component will always reserve place even if the value of that component is empty
 */
class BlankSpaceConfig {
    var productName: Boolean = false
    var rating: Boolean = false
    var price: Boolean = false
    var slashedPrice: Boolean = false
    var discountPercentage: Boolean = false
}