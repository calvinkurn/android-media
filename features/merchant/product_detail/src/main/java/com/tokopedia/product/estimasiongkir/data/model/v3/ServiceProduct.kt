package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceProduct(
        @SerializedName("shipper_name")
        @Expose
        val name: String = "",

        @SerializedName("shipper_id")
        @Expose
        val id: Int = 0,

        @SerializedName("shipper_product_id")
        @Expose
        val productId: Int = 0,

        @SerializedName("shipper_product_name")
        @Expose
        val productName: String = "",

        @SerializedName("shipper_product_desc")
        @Expose
        val productDesc: String = "",

        @SerializedName("shipper_weight")
        @Expose
        val weight: Int = 0,

        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",

        @SerializedName("is_show_map")
        @Expose
        val isShowMap: Int = 0,

        @SerializedName("status")
        @Expose
        val status: Int = 0,

        @SerializedName("recommend")
        @Expose
        val isRecommended: Boolean = false,

        @SerializedName("checksum")
        @Expose
        val checksum: String = "",

        @SerializedName("ut")
        @Expose
        val ut: String = "",

        @SerializedName("price")
        @Expose
        val price: Price = Price(),

        @SerializedName("etd")
        @Expose
        val etd: ETD = ETD(),

        @SerializedName("eta")
        @Expose
        val eta: ETA = ETA(),

        @SerializedName("texts")
        @Expose
        val texts: Texts = Texts(),

        @SerializedName("insurance")
        @Expose
        val insurance: Insurance = Insurance(),

        @SerializedName("error")
        @Expose
        val error: Error = Error(),

        @SerializedName("cod")
        @Expose
        val cod: COD = COD()
)