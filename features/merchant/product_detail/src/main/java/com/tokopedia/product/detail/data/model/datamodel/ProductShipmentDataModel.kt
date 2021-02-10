package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 10/02/21
 */
data class ProductShipmentDataModel(
        val type: String = "",
        val name: String = "",
        var rates: P2RatesEstimateData = P2RatesEstimateData()
) : DynamicPdpDataModel {

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun type(): String = type

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShipmentDataModel) {
            rates == rates
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder
        get() = ImpressHolder()
}