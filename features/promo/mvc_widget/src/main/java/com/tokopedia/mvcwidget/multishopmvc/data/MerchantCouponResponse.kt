package com.tokopedia.mvcwidget.multishopmvc.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class MerchantCouponResponse(

        @SerializedName("productlist")
        val productlist: Productlist? = null
)

data class CatalogMVCWithProductsListItem(

        @SerializedName("shopInfo")
        val shopInfo: ShopInfo? = null,

        @SerializedName("subtitle")
        val subtitle: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("maximumBenefitAmountStr")
        val maximumBenefitAmountStr: String? = null,

        @SerializedName("adInfo")
        val AdInfo: AdInfo? = null,

        @SerializedName("products")
        val products: List<ProductsItem?>? = null
) : BaseItem()

data class ShopInfo(

        @SerializedName("appLink")
        val appLink: String? = null,

        @SerializedName("shopStatusIconURL")
        val shopStatusIconURL: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("iconUrl")
        val iconUrl: String? = null,

        @SerializedName("url")
        val url: String? = null
)

data class ProductCategoriesFilterItem(

        @SerializedName("rootID")
        val rootID: String? = null,

        @SerializedName("rootName")
        val rootName: String? = null
)

data class Category(

        @SerializedName("rootID")
        val rootID: String? = null,

        @SerializedName("rootName")
        val rootName: String? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("name")
        val name: String? = null
)

data class Productlist(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @SerializedName("productCategoriesFilter")
        val productCategoriesFilter: List<ProductCategoriesFilterItem?>? = null,

        @SerializedName("catalogMVCWithProductsList")
        val catalogMVCWithProductsList: List<CatalogMVCWithProductsListItem?>? = null,

        @SerializedName("tokopointsPaging")
        val tokopointsPaging: TokopointsPaging? = null
)

data class ProductsItem(

        @SerializedName("benefitLabel")
        val benefitLabel: String? = null,

        @SerializedName("redirectURL")
        val redirectURL: String? = null,

        @SerializedName("imageURL", alternate = ["image_url"])
        val imageURL: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("id")
        val id: String? = null,

        @SerializedName("redirectAppLink")
        val redirectAppLink: String? = null,

        @SerializedName("category")
        val category: Category? = null
)

data class ResultStatus(

        @SerializedName("code")
        val code: String? = null,

        @SerializedName("status")
        val status: String? = null
)

data class TokopointsPaging(

        @SerializedName("hasNext")
        val hasNext: Boolean? = null
)

data class AdInfo(

        @SerializedName("AdID")
        val AdID: String? = "",

        @SerializedName("AdViewUrl")
        val AdViewUrl: String? = "",

        @SerializedName("AdClickUrl")
        val AdClickUrl: String? = "",
)
