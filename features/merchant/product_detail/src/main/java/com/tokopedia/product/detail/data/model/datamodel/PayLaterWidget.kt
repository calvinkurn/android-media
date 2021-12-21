package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class PayLaterWidget(

    val name: String? = null,
    val type: String? = null,
    val allWidjetList: List<PayLaterModel?>? = null

) : DynamicPdpDataModel {
    override fun type(): String {
        TODO("Not yet implemented")
    }

    override fun type(typeFactory: DynamicProductDetailAdapterFactory?): Int {
        TODO("Not yet implemented")
    }

    override fun name(): String {
        TODO("Not yet implemented")
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        TODO("Not yet implemented")
    }

    override fun newInstance(): DynamicPdpDataModel {
        TODO("Not yet implemented")
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        TODO("Not yet implemented")
    }

    override val impressHolder: ImpressHolder
        get() = TODO("Not yet implemented")

}


data class PayLaterModel(
    val widgetName: String? = null,
    val witgetDetail: String? = null
)
