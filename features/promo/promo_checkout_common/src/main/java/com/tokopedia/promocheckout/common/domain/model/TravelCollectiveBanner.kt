package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.promocheckout.common.data.entity.request.DealsMeta

class TravelCollectiveBanner(@SerializedName("banners")
                       @Expose
                       val banners: List<Banner> = listOf(),
                             @SerializedName("meta")
                       @Expose
                       val dealsMeta: DealsMeta = DealsMeta()) {
    data class Banner(@SerializedName("id")
                      @Expose
                      val id: String = "",
                      @SerializedName("product")
                      @Expose
                      val product: String ="",
                      @SerializedName("attributes")
                      @Expose
                      val attributes: DealsAttributes)

    data class DealsAttributes(@SerializedName("description")
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
                               val promoCode:String = "")

    data class Response(@SerializedName("travelCollectiveBanner")
                        @Expose
                        val response: TravelCollectiveBanner = TravelCollectiveBanner())
}