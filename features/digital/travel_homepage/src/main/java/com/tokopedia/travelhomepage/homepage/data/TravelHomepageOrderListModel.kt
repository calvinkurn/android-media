package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travelhomepage.homepage.presentation.adapter.factory.TravelHomepageAdapterTypeFactory

/**
 * @author by furqan on 06/08/2019
 */
class TravelHomepageOrderListModel(@SerializedName("orders")
                                   @Expose
                                   val orders: List<Order> = listOf(),
                                   @SerializedName("meta")
                                   @Expose
                                   val meta: MetaModel = MetaModel()) {

    data class Order(@SerializedName("product")
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

    data class Response(@SerializedName("TravelCollectiveOrderList")
                        @Expose
                        val response: TravelHomepageOrderListModel = TravelHomepageOrderListModel())

}