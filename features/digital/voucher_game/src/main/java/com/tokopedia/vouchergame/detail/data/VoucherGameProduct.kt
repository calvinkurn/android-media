package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapterFactory

/**
 * Created by resakemal on 13/08/19.
 */
class VoucherGameProduct(

        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes(),
        var selected: Boolean = false

): Visitable<VoucherGameDetailAdapterFactory> {

        override fun type(typeFactory: VoucherGameDetailAdapterFactory) = typeFactory.type(this)

        class Attributes(
                @SerializedName("price")
                @Expose
                val price: String = "",
                @SerializedName("detail")
                @Expose
                val detail: String = "",
                @SerializedName("info")
                @Expose
                val info: String = "",
                @SerializedName("promo")
                @Expose
                val promo: Promo? = Promo()
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
                val valueText: String = ""
        )
}