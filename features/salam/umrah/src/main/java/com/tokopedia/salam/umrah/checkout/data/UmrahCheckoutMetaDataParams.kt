package com.tokopedia.salam.umrah.checkout.data

class UmrahCheckoutMetaDataParams(
        val userId: String = "",
        val productId: String = "",
        val price: Int = 0,
        val productVariantId: String = "",
        val contact: Contact = Contact()

)

class Contact(
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val pilgrims: List<Pilgrims> = emptyList()
)

class Pilgrims(
        val title: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val birthDate: String = ""

)