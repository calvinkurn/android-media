package com.tokopedia.vouchergame.list.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 13/08/19.
 */
class VoucherGameProduct(

        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes()

) {
        class Attributes(
                @SerializedName("price")
                @Expose
                val price: String = "",
                @SerializedName("detaul")
                @Expose
                val detaul: String = "",
                @SerializedName("info")
                @Expose
                val info: String = "",
                @SerializedName("promo")
                @Expose
                val promo: Promo = Promo()
        )

        class Promo(
                @SerializedName("id")
                @Expose
                val id: String = "",
                @SerializedName("bonus_text")
                @Expose
                val bonusText: String = "",
                @SerializedName("new_price")
                @Expose
                val newPrice: String = "",
                @SerializedName("new_price_plain")
                @Expose
                val newPricePlain: Int = 0,
                @SerializedName("tag")
                @Expose
                val tag: String = "",
                @SerializedName("terms")
                @Expose
                val terms: String = "",
                @SerializedName("value_text")
                @Expose
                val vzlueText: String = ""
        )
}