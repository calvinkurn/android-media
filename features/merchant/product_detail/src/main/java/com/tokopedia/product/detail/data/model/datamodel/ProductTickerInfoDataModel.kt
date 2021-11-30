package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 08/06/20
 */
data class ProductTickerInfoDataModel(
        val type: String = "",
        val name: String = "",
        var tickerDataResponse: List<TickerDataResponse> = listOf()
) : DynamicPdpDataModel {

    override fun type(): String = type
    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    fun getComponentTrackData(adapterPosition: Int) = ComponentTrackDataModel(type, name, adapterPosition + 1)

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductTickerInfoDataModel) {
            tickerDataResponse.hashCode() == newData.tickerDataResponse.hashCode()
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }
}