package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageBannerModel(@SerializedName("banners")
                                @Expose
                                val banners: List<Banner> = listOf(),
                                @SerializedName("meta")
                                @Expose
                                val meta: MetaModel = MetaModel()) : TravelHomepageItemModel() {

    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)

    data class Banner(@SerializedName("id")
                      @Expose
                      val id: String = "",
                      @SerializedName("product")
                      @Expose
                      val product: String = "",
                      @SerializedName("attributes")
                      @Expose
                      val attribute: Attribute = Attribute())

    data class Attribute(@SerializedName("description")
                         @Expose
                         val description: String = "",
                         @SerializedName("webURL")
                         @Expose
                         val webUrl: String = "",
                         @SerializedName("appURL")
                         @Expose
                         val appUrl: String = "",
                         @SerializedName("imageURL")
                         @Expose
                         val imageUrl: String = "",
                         @SerializedName("promoCode")
                         @Expose
                         val promoCode: String = "")

    data class Response(@SerializedName("travelCollectiveBanner")
                        @Expose
                        val response: TravelHomepageBannerModel = TravelHomepageBannerModel())

}