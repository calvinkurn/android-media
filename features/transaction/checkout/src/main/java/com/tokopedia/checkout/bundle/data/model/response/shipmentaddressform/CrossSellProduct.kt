package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 25/10/21.
 */
data class CrossSellProduct (
        @SerializedName("icon_desktop")
        val iconDesktop: String = "",

        @SerializedName("icon_mobile")
        val iconMobile: String = "",

        @SerializedName("image_desktop")
        val imageDesktop: String = "",

        @SerializedName("lang")
        val lang: String = "",

        @SerializedName("max_retail_price")
        val maxRetailPrice: Int = 0,

        @SerializedName("meta_data")
        val metaData: String = "",

        @SerializedName("more_info_icon_desktop")
        val moreInfoIconDesktop: String = "",

        @SerializedName("more_info_icon_mobile")
        val moreInfoIconMobile: String = "",

        @SerializedName("price_tier")
        val priceTier: CrossSellProductPriceTier = CrossSellProductPriceTier(),

        @SerializedName("product_id")
        val productId: String = "",

        @SerializedName("product_code")
        val productCode: Int = 0,

        @SuppressLint("Invalid Data Type")
        @SerializedName("product_info_type_id")
        val productInfoTypeId: Long = 0,

        @SerializedName("product_info_type_name")
        val productInfoTypeName: String = "",

        @SerializedName("quantity")
        val quantity: Int = 0,

        @SerializedName("sales_price")
        val salesPrice: Int = 0,

        @SerializedName("saving")
        val saving: String = "",

        @SuppressLint("Invalid Data Type")
        @SerializedName("vertical_id")
        val verticalId: Long = 0,

        @SerializedName("with_image")
        val withImage: Boolean = false,

        @SerializedName("title")
        val title: String = "",

        @SerializedName("subtitle")
        val subtitle: String = "",

        @SerializedName("ticker_text")
        val tickerText: String = "",

        @SerializedName("tooltip_text")
        val tooltipText: String = "",

        @SerializedName("category_id")
        val categoryId: String = "")