package com.tokopedia.product.detail.component.shipment

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.ShipmentBody
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ShipmentUiModel(
    val type: String,
    val name: String
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

    fun setData(rates: P2RatesEstimate?) {
        if (rates == null) {
            state = Failed
            return
        }

        val data = rates.p2RatesData
        val errors = data.p2RatesError
        if (errors.isNotEmpty()) {
            state = Error(
                title = data.title,
                subtitle = data.subtitle,
                background = rates.background,
                errorCode = errors.firstOrNull()?.errorCode ?: 0
            )
            return
        }

        Success(
            logo = data.boBadge.imageUrl,
            title = data.title,
            slashPrice = data.originalShippingRate.getCurrencyFormatted() ?: "",
            appLink = "",
            background = rates.background,
            body = data.shipmentBody.toUiModel()
        ).also { state = it }
    }

    private fun List<ShipmentBody>.toUiModel(): List<Info> = map { body ->
        val logo = body.icon.toIntOrNull() ?: -1
        Info(
            logo = logo,
            text = body.text
        )
    }

    var state: State = Loading

    sealed interface State

    object Loading : State

    data class Success(
        val logo: String,
        val title: String,
        val slashPrice: String,
        val appLink: String,
        val background: String,
        val body: List<Info>
    ) : State

    data class Info(
        val logo: Int,
        val text: String
    )

    data class Error(
        val title: String,
        val subtitle: String,
        val background: String,
        val errorCode: Int
    ) : State

    object Failed : State

}
