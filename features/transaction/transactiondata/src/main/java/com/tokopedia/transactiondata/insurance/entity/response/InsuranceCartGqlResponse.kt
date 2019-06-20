package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class InsuranceCartGqlResponse(
        @SerializedName("cart_list_transactional")
        val data: InsuranceCartResponse
)

data class InsuranceCartResponse(
        @SerializedName("shops")
        val cartShopsList: ArrayList<InsuranceCartShops>
)

data class InsuranceCartShops(
        @SerializedName("shop_id")
        val shopId: Long,

        @SerializedName("items")
        val shopIemsList: ArrayList<InsuranceCartShopItems>
)

data class InsuranceCartShopItems(
        @SerializedName("product_id")
        val productId: Long,

        @SerializedName("digital_product")
        val digitalProductList: ArrayList<InsuranceCartDigitalProduct>
)

data class InsuranceCartDigitalProduct(
        @SerializedName("digital_product_id")
        val digitalProductId: Long,

        @SerializedName("cart_item_id")
        val cartItemId: Long,

        @SerializedName("type_id")
        val typeId: Long,

        @SerializedName("price_per_product")
        val pricePerProduct: Long,

        @SerializedName("total_price")
        val totalPrice: Long,

        @SerializedName("opt_in")
        val optIn: Boolean,

        @SerializedName("is_product_level")
        val isProductLevel: Boolean,

        @SerializedName("is_seller_money")
        val isSellerMoney: Boolean,

        @SerializedName("is_application_needed")
        val isApplicationNeeded: Boolean,

        @SerializedName("is_new")
        val isNew: Boolean,

        @SerializedName("product_info")
        val productInfo: InsuranceCartProductInfo,

        @SerializedName("application_details")
        val applicationDetails: ArrayList<InsuranceProductApplicationDetails>
)

data class InsuranceCartProductInfo(
        @SerializedName("title")
        val title: String,

        @SerializedName("sub_title")
        val subTitle: String,

        @SerializedName("link_detail_info_title")
        val linkDetailInfoTitle: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("icon_url")
        val iconUrl: String,

        @SerializedName("web_link_html")
        val webLinkHtml: String,

        @SerializedName("ticker_text")
        val tickerText: String
)

data class InsuranceProductApplicationDetails(
        @SerializedName("id")
        val id: Int,

        @SerializedName("label")
        val label: String,

        @SerializedName("place_holder")
        val placeHolder: String,

        @SerializedName("required")
        val isRequired: Boolean,

        @SerializedName("value")
        val value: String,

        @SerializedName("values")
        val valuesList: ArrayList<InsuranceApplicationValue>,

        @SerializedName("validations")
        val validationsList: ArrayList<InsuranceApplicationValidation>
)

data class InsuranceApplicationValue(
        @SerializedName("id")
        val valuesId: Int,

        @SerializedName("value")
        val value: String
)

data class InsuranceApplicationValidation(
        @SerializedName("id")
        val validationId: Int,

        @SerializedName("type")
        val type: String,

        @SerializedName("value")
        val validationValue: String,

        @SerializedName("error_message")
        val validationErrorMessage: String
)