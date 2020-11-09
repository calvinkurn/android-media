package com.tokopedia.promocheckout.list.model.listtravelcollectivebanner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PromoChekoutDealsBannerModel(@SerializedName("banners")
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
                      val attribute: Attribute = Attribute())

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
                         val type: String = "")

    data class Response(@SerializedName("travelCollectiveBanner")
                        @Expose
                        val response: PromoChekoutDealsBannerModel = PromoChekoutDealsBannerModel())
}