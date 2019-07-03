package com.tokopedia.transactiondata.insurance.entity.response

import com.google.gson.annotations.SerializedName

data class InsuranceCartGqlResponse(
        @SerializedName("cart_list_transactional")
        var data: InsuranceCartResponse
)

data class InsuranceCartResponse(
        @SerializedName("shops")
        var cartShopsList: ArrayList<InsuranceCartShops>
)

data class InsuranceCartShops(
        @SerializedName("shop_id")
        var shopId: Long,

        @SerializedName("items")
        var shopItemsList: ArrayList<InsuranceCartShopItems>
)

data class InsuranceCartShopItems(
        @SerializedName("product_id")
        var productId: Long,

        @SerializedName("digital_product")
        var digitalProductList: ArrayList<InsuranceCartDigitalProduct>
)

data class InsuranceCartDigitalProduct(
        @SerializedName("digital_product_id")
        var digitalProductId: Long,

        @SerializedName("cart_item_id")
        var cartItemId: Long,

        @SerializedName("type_id")
        var typeId: Long,

        @SerializedName("price_per_product")
        var pricePerProduct: Long,

        @SerializedName("total_price")
        var totalPrice: Long,

        @SerializedName("opt_in")
        var optIn: Boolean,

        @SerializedName("is_product_level")
        var isProductLevel: Boolean,

        @SerializedName("is_seller_money")
        var isSellerMoney: Boolean,

        @SerializedName("is_application_needed")
        var isApplicationNeeded: Boolean,

        @SerializedName("is_new")
        var isNew: Boolean,

        @SerializedName("product_info")
        var productInfo: InsuranceCartProductInfo,

        @SerializedName("application_details")
        var applicationDetails: ArrayList<InsuranceProductApplicationDetails>
)

data class InsuranceCartProductInfo(
        @SerializedName("title")
        var title: String,

        @SerializedName("sub_title")
        var subTitle: String,

        @SerializedName("link_detail_info_title")
        var linkDetailInfoTitle: String,

        @SerializedName("description")
        var description: String,

        @SerializedName("icon_url")
        var iconUrl: String,

        @SerializedName("web_link_html")
        var webLinkHtml: String,

        @SerializedName("ticker_text")
        var tickerText: String
)

data class InsuranceProductApplicationDetails(
        @SerializedName("id")
        var id: Int,

        @SerializedName("label")
        var label: String,

        @SerializedName("place_holder")
        var placeHolder: String,

        @SerializedName("type")
        var type: String,

        @SerializedName("required")
        var isRequired: Boolean,

        @SerializedName("value")
        var value: String,

        @SerializedName("values")
        var valuesList: ArrayList<InsuranceApplicationValue>,

        @SerializedName("validations")
        var validationsList: ArrayList<InsuranceApplicationValidation>
)

data class InsuranceApplicationValue(
        @SerializedName("id")
        var valuesId: Int,

        @SerializedName("value")
        var value: String
)

data class InsuranceApplicationValidation(
        @SerializedName("id")
        var validationId: Int,

        @SerializedName("type")
        var type: String,

        @SerializedName("value")
        var validationValue: String,

        @SerializedName("error_message")
        var validationErrorMessage: String
)