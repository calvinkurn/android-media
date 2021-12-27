package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class PayLaterWidgetDataModel(

    val name: String = "",
    val type: String = "",
    val widgetData: List<PayLaterWidgetList?> = listOf()

) : DynamicPdpDataModel {
    override fun type(): String {
        return type
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String {
        return name
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PayLaterWidgetDataModel) {
            widgetData.hashCode() == newData.widgetData.hashCode()
        } else
            return false
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        // TODO change the payload implementation
        return bundle
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

}


data class PayLaterWidgetList(
    val widgetName: String? = null,
    val witgetDetail: String? = null
)
