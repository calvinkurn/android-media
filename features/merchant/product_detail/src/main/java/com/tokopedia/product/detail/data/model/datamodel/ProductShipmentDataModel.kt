package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.utils.resources.isDarkMode

/**
 * Created by Yehezkiel on 10/02/21
 */
@Deprecated("Replaced with Shipment V3")
data class ProductShipmentDataModel(
        val type: String = "",
        val name: String = "",
        var rates: P2RatesEstimateData = P2RatesEstimateData(),
        var isFullfillment: Boolean = false,
        var freeOngkirUrl: String = "",
        var freeOngkirType: Int = 0,
        var tokoCabangIconUrl: String = "",
        var isCod: Boolean = false,
        var shouldShowShipmentError: Boolean = false,
        var isTokoNow: Boolean = false,
        var shipmentPlusData: ShipmentPlusData = ShipmentPlusData()
) : DynamicPdpDataModel {

    fun isBoeType(): Boolean {
        return freeOngkirType == ProductDetailCommonConstant.BEBAS_ONGKIR_EXTRA
    }

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun type(): String = type

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShipmentDataModel) {
            rates == newData.rates &&
                    isFullfillment == newData.isFullfillment
                    && isCod == newData.isCod
                    && freeOngkirUrl == newData.freeOngkirUrl
                    && shouldShowShipmentError == newData.shouldShowShipmentError
                    && freeOngkirType == newData.freeOngkirType
                    && tokoCabangIconUrl == newData.tokoCabangIconUrl
                    && shipmentPlusData == newData.shipmentPlusData
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()
}

data class ShipmentPlusData(
    val isShow: Boolean = false,
    val text: String = "",
    val action: String = "",
    val actionLink: String = "",
    val trackerData: TrackerData = TrackerData(),
    private val logoUrl: String = "",
    private val logoUrlDark: String = "",
    private val bgUrl: String = "",
    private val bgUrlDark: String = ""
) {
    fun getLogoUrl(context: Context): String {
        return if (context.isDarkMode()) logoUrlDark else logoUrl
    }

    fun getBackgroundUrl(context: Context): String {
        return if (context.isDarkMode()) bgUrlDark else bgUrl
    }

    data class TrackerData(
        val isPlus: Boolean = false
    )
}
