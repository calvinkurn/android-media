package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

class ProductNotifyMeDataModel(
        val type: String = "",
        val name: String = "",
        var campaignID: String = "",
        var campaignType: String = "",
        var campaignTypeName: String = "",
        var endDate: String = "",
        var startDate: String = "",
        var notifyMe: Boolean = false,
        var isUpcomingDealsType:Boolean = false
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}