package com.tokopedia.product.detail.component.shipment

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ShipmentUiModel(
    val type: String,
    val name: String,
    var data: Data? = null
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        // TODO vindo - complete this
        return false
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()

    data class Data(
        val logo: String,
        val price: String,
        val slashPrice: String,
        val appLink: String,
        val background: String,
        val body: List<Info>
    )

    data class Info(
        val logo: Int,
        val text: String
    )

}
