package com.tokopedia.salam.umrah.checkout.data

class UmrahCheckoutMetaDataParams(
        val user_id: Int = 0,
        val product_id: Int = 0,
        val price: Int = 0,
        val product_variant_id: Int = 0,
        val contact: Contact = Contact(),
        val pilgrims: List<Pilgrims> = emptyList(),
        val payment_terms : List<TermParam> = emptyList()

)

class Contact(
        val name: String = "",
        val email: String = "",
        val phone: String = ""
)

class Pilgrims(
        val title: String = "",
        val first_name: String = "",
        val last_name: String = "",
        val birth_date: String = ""
)

class TermParam(
        val type: Int = 0,
        val price: Int = 0,
        val due_date: String = ""
)
