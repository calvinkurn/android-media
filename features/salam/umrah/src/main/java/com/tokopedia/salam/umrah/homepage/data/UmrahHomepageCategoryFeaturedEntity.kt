package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

data class UmrahHomepageCategoryFeaturedEntity (
        @SerializedName("umrahCategories")
        @Expose
        val umrahCategoriesFeatured:List<UmrahCategoriesFeatured> = emptyList()
):UmrahHomepageModel() {
        companion object{
        }

        override fun type(typeFactory: UmrahHomepageFactory): Int {
                return typeFactory.type(this)
        }
}
data class UmrahCategoriesFeatured(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("products")
        @Expose
        val products: List<Products> =  arrayListOf()
)


