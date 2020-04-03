package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model

import com.google.gson.annotations.SerializedName

/**
 * @author by Rafli Syam on 2020-03-09
 */

data class GetProductListFilter (
        @SerializedName("page") var page : Int = 1,
        @SerializedName("perPage") var perPage : Int = 10,
        @SerializedName("fkeyword") var fkeyword : String = "",
        @SerializedName("fmenu") var fmenu : String? = "",
        @SerializedName("fmenuExclude") var fmenuExclude : String? = "",
        @SerializedName("fcategory") var fcategory : Int = 0,
        @SerializedName("fcondition") var fcondition : Int = 0,
        @SerializedName("fcatalog") var fcatalog : Int = 0,
        @SerializedName("fpicture") var fpicture : Int = 0,
        @SerializedName("sort") var sort : Int = 0
) {

    /**
     * Set back filter to default value, for now its just for keyword and page
     */
    fun setDefaultFilter() {
        page = 1
        fkeyword = ""
    }
}