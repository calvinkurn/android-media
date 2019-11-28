package com.tokopedia.common.topupbills.data.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * Created by resakemal on 26/11/19.
 */
open class CatalogProductData(

        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes()

) {
        class Attributes(
                @SerializedName("desc")
                @Expose
                val desc: String = "",
                @SerializedName("price")
                @Expose
                val price: String = "",
                @SerializedName("price_plain")
                @Expose
                val pricePlain: String = "",
                @SerializedName("promo")
                @Expose
                val promo: Promo? = Promo(),
                @SerializedName("product_labels")
                @Expose
                val productLabels: List<String> = listOf(),
                @SerializedName("detail")
                @Expose
                val detail: String = "",
                @SerializedName("detail_compat")
                @Expose
                val detailCompat: String = "",
                @SerializedName("detail_url")
                @Expose
                val detailUrl: String = "",
                @SerializedName("detail_url_text")
                @Expose
                val detailUrlText: String = ""
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