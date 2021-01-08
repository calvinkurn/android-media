package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by Rafli Syam on 2020-03-09
 */

data class GetProductListFilter (
        @Expose
        @SerializedName("page") var page : Int = 1,
        @Expose
        @SerializedName("perPage") var perPage : Int = 10,
        @Expose
        @SerializedName("fkeyword") var fkeyword : String = "",
        @Expose
        @SerializedName("fmenu") var fmenu : String? = "",
        @Expose
        @SerializedName("fmenuExclude") var fmenuExclude : String? = "",
        @Expose
        @SerializedName("fcategory") var fcategory : Int = 0,
        @Expose
        @SerializedName("fcondition") var fcondition : Int = 0,
        @Expose
        @SerializedName("fcatalog") var fcatalog : Int = 0,
        @Expose
        @SerializedName("fpicture") var fpicture : Int = 0,
        @Expose
        @SerializedName("sort") var sort : Int = 1
) {

    /**
     * Set back filter to default value, for now its just for keyword and page
     */
    fun setDefaultFilter() {
        page = 1
        fkeyword = ""
    }
}