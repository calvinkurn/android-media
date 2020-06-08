package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageDestinationModel(@SerializedName("destination")
                                     @Expose
                                     val destination: List<Destination> = arrayListOf(),
                                     @SerializedName("meta")
                                     @Expose
                                     val meta: MetaModel = MetaModel(),
                                     val spanSize: Int = 1)
    : TravelHomepageItemModel() {

    override fun type(typeFactory: TravelHomepageAdapterTypeFactory): Int = typeFactory.type(this)

    class Destination(@SerializedName("attributes")
                      @Expose
                      val attributes: Attribute = Attribute())

    data class Attribute(var id: String = "",
                        @SerializedName("title")
                         @Expose
                         var title: String = "",
                         @SerializedName("subtitle")
                         @Expose
                         var subtitle: String = "",
                         @SerializedName("webURL")
                         @Expose
                         var webUrl: String = "",
                         @SerializedName("appURL")
                         @Expose
                         var appUrl: String = "",
                         @SerializedName("imageURL")
                         @Expose
                         var imageUrl: String = "")

    data class Response(@SerializedName("TravelDestination")
                        @Expose
                        val response: TravelHomepageDestinationModel = TravelHomepageDestinationModel())

    data class MetaModel(@SerializedName("title")
                         @Expose
                         val title: String = "")
}