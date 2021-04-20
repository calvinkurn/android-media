package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/08/2019
 */
class TravelCollectiveBannerModel(@SerializedName("banners")
                                  @Expose
                                  val banners: List<Banner> = listOf(),
                                  @SerializedName("meta")
                                  @Expose
                                  val meta: MetaModel = MetaModel()) {

    data class Banner(@SerializedName("id")
                      @Expose
                      var id: String = "0",
                      @SerializedName("product")
                      @Expose
                      var product: String = "",
                      @SerializedName("attributes")
                      @Expose
                      val attribute: Attribute = Attribute(),

                      var position: Int = 0)

    data class Attribute(@SerializedName("description")
                         @Expose
                         var description: String = "",
                         @SerializedName("webURL")
                         @Expose
                         var webUrl: String = "",
                         @SerializedName("appURL")
                         @Expose
                         var appUrl: String = "",
                         @SerializedName("imageURL")
                         @Expose
                         var imageUrl: String = "",
                         @SerializedName("promoCode")
                         @Expose
                         var promoCode: String = "")

    data class MetaModel(@SerializedName("title")
                         @Expose
                         val title: String = "",
                         @SerializedName("webURL")
                         @Expose
                         val webUrl: String = "",
                         @SerializedName("appURL")
                         @Expose
                         val appUrl: String = "",
                         @SerializedName("type")
                         @Expose
                         val type: String = "",
                         @SerializedName("label")
                         @Expose
                         val label: String = "")

    data class Response(@SerializedName("travelCollectiveBanner")
                        @Expose
                        val response: TravelCollectiveBannerModel = TravelCollectiveBannerModel())
}