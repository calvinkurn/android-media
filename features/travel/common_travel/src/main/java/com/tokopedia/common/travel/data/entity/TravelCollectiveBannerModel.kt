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
                         val type: String = "")

    data class Response(@SerializedName("travelCollectiveBanner")
                        @Expose
                        val response: TravelCollectiveBannerModel = TravelCollectiveBannerModel())
}