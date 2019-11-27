package com.tokopedia.salam.umrah.pdp.data
/**
 * @author by M on 12/11/2019
 */
data class ParamPurchase(
        var name: String = "",
        var id: String = "",
        var price: Int = 0,
        var slugName: String = "",
        var variant: String = "",
        var totalPassenger: Int = 1,
        var shopId: String = "",
        var shopName: String = "",
        var categoryId: String = "",
        var totalPrice: Int = 0,
        var departureDate: String = "",
        var variantId: String = ""
)