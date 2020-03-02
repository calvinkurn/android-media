package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageCategoryListModel(@SerializedName("category")
                                      @Expose
                                      val categories: List<Category> = arrayListOf()) : TravelHomepageItemModel() {

    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)

    data class Category(@SerializedName("product")
                        @Expose
                        val product: String = "",
                        @SerializedName("attributes")
                        @Expose
                        val attributes: Attribute = Attribute())

    data class Attribute(@SerializedName("title")
                         @Expose
                         val title: String = "",
                         @SerializedName("webURL")
                         @Expose
                         val webUrl: String = "",
                         @SerializedName("appURL")
                         @Expose
                         val appUrl: String = "",
                         @SerializedName("imageURL")
                         @Expose
                         val imageUrl: String = "")

    data class Response(@SerializedName("travelCategoryList")
                        @Expose
                        val response: TravelHomepageCategoryListModel = TravelHomepageCategoryListModel())
}