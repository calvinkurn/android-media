package com.tokopedia.salam.umrah.checkout.data

class UmrahCheckoutResultParams (
       val carts : Carts = Carts()
)

class Carts(
        val meta_data: String = "",
        val business_type: Int = 3,
        val cart_info: Array<String> = arrayOf()
)