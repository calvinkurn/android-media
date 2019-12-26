package com.tokopedia.salam.umrah.checkout.data


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutResultParams (
        val carts : Carts = Carts()
)

class Carts(
        val meta_data: String = "",
        val business_type: Int = 3,
        val cart_info: Array<String> = arrayOf()
)