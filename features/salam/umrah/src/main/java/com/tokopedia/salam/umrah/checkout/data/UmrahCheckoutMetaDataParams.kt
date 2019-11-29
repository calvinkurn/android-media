package com.tokopedia.salam.umrah.checkout.data

class UmrahCheckoutMetaDataParams(
        val user_id: String = "",
        val product_id: String = "",
        val price: Int = 0,
        val product_variant_id: String = "",
        val contact: Contact = Contact(),
        val payment_terms : List<Term> = emptyList()

)

class Contact(
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val pilgrims: List<Pilgrims> = emptyList()
)

class Pilgrims(
        val title: String = "",
        val first_name: String = "",
        val last_name: String = "",
        val birth_date: String = ""
)
