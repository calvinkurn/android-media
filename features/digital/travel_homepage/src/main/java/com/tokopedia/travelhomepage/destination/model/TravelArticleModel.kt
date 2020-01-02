package com.tokopedia.travelhomepage.destination.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageItemModel
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory

/**
 * @author by jessica on 2019-12-23
 */

data class TravelArticleModel (
        @SerializedName("items")
        @Expose
        val items: List<Item> = listOf(),

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
)//: TravelHomepageItemModel()
{

//    override fun type(typeFactory: TravelDestinationAdapterTypeFactory): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    data class Response(
        @SerializedName("TravelArticleModel")
        @Expose
        val response: TravelArticleModel = TravelArticleModel()
    )

    data class Item (
        @SerializedName("tag")
        @Expose
        val tag: Int = 0,

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("publishedDate")
        @Expose
        val publishedDate: String = "",

        @SerializedName("webURL")
        @Expose
        val webUrl: String = "",

        @SerializedName("appURL")
        @Expose
        val appUrl: String = "",

        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = ""
    )

    data class Meta(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("URLTitle")
        @Expose
        val urlTitle: String = "",

        @SerializedName("webURL")
        @Expose
        val webUrl: String = "",

        @SerializedName("appURL")
        @Expose
        val appUrl: String = ""
    )
}