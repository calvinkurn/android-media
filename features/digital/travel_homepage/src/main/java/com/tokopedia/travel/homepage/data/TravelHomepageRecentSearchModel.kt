package com.tokopedia.travel.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travel.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageRecentSearchModel(@SerializedName("items")
                                      @Expose
                                      val items: List<Item> = arrayListOf(),
                                      @SerializedName("meta")
                                      @Expose
                                      val meta: MetaModel = MetaModel()) : TravelHomepageItemModel() {

    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)

    data class Item(@SerializedName("product")
                    @Expose
                    val product: String = "",
                    @SerializedName("title")
                    @Expose
                    val title: String = "",
                    @SerializedName("subtitle")
                    @Expose
                    val subtitle: String = "",
                    @SerializedName("prefix")
                    @Expose
                    val prefix: String = "",
                    @SerializedName("prefixStyling")
                    @Expose
                    val prefixStyling: String = "normal",
                    @SerializedName("value")
                    @Expose
                    val value: String = "",
                    @SerializedName("webURL")
                    @Expose
                    val webUrl: String = "",
                    @SerializedName("appURL")
                    @Expose
                    val appUrl: String = "",
                    @SerializedName("imageURL")
                    @Expose
                    val imageUrl: String = "")

}