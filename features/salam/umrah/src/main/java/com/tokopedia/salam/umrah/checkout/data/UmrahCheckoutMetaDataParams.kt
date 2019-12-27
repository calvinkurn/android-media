package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutMetaDataParams(
        @SerializedName("user_id")
        @Expose
        val user_id: Int = 0,
        @SerializedName("product_id")
        @Expose
        val product_id: Int = 0,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("product_variant_id")
        @Expose
        val product_variant_id: Int = 0,
        @SerializedName("contact")
        @Expose
        val contact: Contact = Contact(),
        @SerializedName("pilgrims")
        @Expose
        val pilgrims: List<Pilgrims> = emptyList(),
        @SerializedName("payment_terms")
        @Expose
        val payment_terms : List<TermParam> = emptyList()

)

class Contact(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = ""
)

class Pilgrims(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("first_name")
        @Expose
        val first_name: String = "",
        @SerializedName("last_name")
        @Expose
        val last_name: String = "",
        @SerializedName("birth_date")
        @Expose
        val birth_date: String = ""
)

class TermParam(
        @SerializedName("type")
        @Expose
        val type: Int = 0,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("due_date")
        @Expose
        val due_date: String = ""
)