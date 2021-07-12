package com.tokopedia.tokopoints.view.model.merchantcoupon

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class MerchantCouponResponse(

        @field:SerializedName("productlist")
        val productlist: Productlist? = null
)

data class CatalogMVCWithProductsListItem(

        @field:SerializedName("shopInfo")
        val shopInfo: ShopInfo? = null,

        @field:SerializedName("subtitle")
        val subtitle: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("maximumBenefitAmountStr")
        val maximumBenefitAmountStr: String? = null,

        @field:SerializedName("adInfo")
        val AdInfo: AdInfo? = null,

        @field:SerializedName("products")
        val products: List<ProductsItem?>? = null
) : BaseItem()

data class ShopInfo(

        @field:SerializedName("appLink")
        val appLink: String? = null,

        @field:SerializedName("shopStatusIconURL")
        val shopStatusIconURL: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("iconUrl")
        val iconUrl: String? = null,

        @field:SerializedName("url")
        val url: String? = null
)

data class ProductCategoriesFilterItem(

        @field:SerializedName("rootID")
        val rootID: String? = null,

        @field:SerializedName("rootName")
        val rootName: String? = null
)

data class Category(

        @field:SerializedName("rootID")
        val rootID: String? = null,

        @field:SerializedName("rootName")
        val rootName: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("name")
        val name: String? = null
)

data class Productlist(

        @field:SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @field:SerializedName("productCategoriesFilter")
        val productCategoriesFilter: List<ProductCategoriesFilterItem?>? = null,

        @field:SerializedName("catalogMVCWithProductsList")
        val catalogMVCWithProductsList: List<CatalogMVCWithProductsListItem?>? = null,

        @field:SerializedName("tokopointsPaging")
        val tokopointsPaging: TokopointsPaging? = null
)

data class ProductsItem(

        @field:SerializedName("benefitLabel")
        val benefitLabel: String? = null,

        @field:SerializedName("redirectURL")
        val redirectURL: String? = null,

        @field:SerializedName("imageURL")
        val imageURL: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("redirectAppLink")
        val redirectAppLink: String? = null,

        @field:SerializedName("category")
        val category: Category? = null
)

data class ResultStatus(

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("status")
        val status: String? = null
)

data class TokopointsPaging(

        @field:SerializedName("hasNext")
        val hasNext: Boolean? = null
)

data class AdInfo(

        @field:SerializedName("AdID")
        val AdID: String? = null,

        @field:SerializedName("AdViewUrl")
        val AdViewUrl: String? = null,

        @field:SerializedName("AdClickUrl")
        val AdClickUrl: String? = null,
)
