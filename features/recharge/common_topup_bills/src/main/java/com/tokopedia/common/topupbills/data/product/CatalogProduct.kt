package com.tokopedia.common.topupbills.data.product

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 26/11/19.
 */
open class CatalogProduct(

        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("attributes")
        @Expose
        var attributes: Attributes = Attributes()

) {
        class Attributes(
                @SerializedName("desc")
                @Expose
                var desc: String = "",
                @SuppressLint("Invalid Data Type")
                @SerializedName("price")
                @Expose
                var price: String = "",
                @SerializedName("price_plain")
                @Expose
                var pricePlain: String = "",
                @SerializedName("promo")
                @Expose
                var promo: Promo? = Promo(),
                @SerializedName("product_labels")
                @Expose
                var productLabels: List<String> = listOf(),
                @SerializedName("detail")
                @Expose
                var detail: String = "",
                @SerializedName("detail_compact")
                @Expose
                var detailCompact: String = "",
                @SerializedName("detail_url")
                @Expose
                var detailUrl: String = "",
                @SerializedName("detail_url_text")
                @Expose
                var detailUrlText: String = "",
                @SerializedName("category_id")
                @Expose
                val categoryId: String = "0",
                @SerializedName("operator_id")
                @Expose
                val operatorId: String = "0",
        )

        class Promo(
                @SerializedName("id")
                @Expose
                var id: String = "",
                @SerializedName("bonus_text")
                @Expose
                var bonusText: String = "",
                @SerializedName("new_price")
                @Expose
                var newPrice: String = "",
                @SerializedName("new_price_plain")
                @Expose
                var newPricePlain: Int = 0,
                @SerializedName("tag")
                @Expose
                var tag: String = "",
                @SerializedName("terms")
                @Expose
                var terms: String = "",
                @SerializedName("value_text")
                @Expose
                var valueText: String = "",
                @SerializedName("discount")
                @Expose
                var discount: String = "",
        )
}