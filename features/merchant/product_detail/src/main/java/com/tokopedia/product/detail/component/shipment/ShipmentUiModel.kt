package com.tokopedia.product.detail.component.shipment

import android.content.Context
import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirType
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.ShipmentBody
import com.tokopedia.product.detail.common.data.model.rates.ShipmentPlus
import com.tokopedia.product.detail.component.shipment.ShipmentUiModel.Ticker.mapTickerType
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.product.detail.common.data.model.rates.Ticker as ratesTicker

data class ShipmentUiModel(
    val type: String,
    val name: String
) : DynamicPdpDataModel {
    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return newData is ShipmentUiModel &&
            state == newData.state
    }

    override fun newInstance(): DynamicPdpDataModel = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null

    override val impressHolder: ImpressHolder = ImpressHolder()

    fun setData(
        rates: P2RatesEstimate?,
        isCod: Boolean,
        boType: Int
    ) {
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

        val slashPrice = data.originalShippingRate.takeIf { it > 0 }?.getCurrencyFormatted() ?: ""

        Success(
            logo = data.boBadge.imageUrl,
            logoHeight = data.boBadge.imageHeight,
            title = data.title,
            slashPrice = slashPrice,
            background = rates.background,
            body = data.shipmentBody.toUiModel(),
            tickers = data.tickers.toTickerData(),
            tips = data.tickers.toTips(),
            shipmentPlus = rates.shipmentPlus.toUiModel(boType),
            labels = data.chipsLabel,
            isCod = isCod,
            isScheduled = data.isScheduled
        ).also { state = it }
    }

    private fun List<ShipmentBody>.toUiModel(): List<Info> = map { body ->
        val logo = body.icon.toIntOrNull() ?: -1
        Info(
            logo = logo,
            text = body.text
        )
    }

    private fun List<ratesTicker>.toTickerData(): List<TickerData> = mapNotNull { ticker ->
        if (ticker.color in Ticker.types) {
            TickerData(
                description = ticker.message,
                type = mapTickerType(ticker.color),
                title = ticker.title,
                isFromHtml = true
            )
        } else {
            null
        }
    }

    private fun List<ratesTicker>.toTips(): Tips? = firstOrNull {
        it.color in Tips.types
    }?.let { ticker ->
        Tips(
            title = ticker.title,
            message = ticker.message,
            link = ticker.link,
            action = ticker.action
        )
    }

    private fun ShipmentPlus?.toUiModel(boType: Int): ShipmentPlusData {
        return if (this == null) {
            ShipmentPlusData()
        } else {
            val isPlus = boType == BebasOngkirType.BO_PLUS.value ||
                boType == BebasOngkirType.BO_PLUS_DT.value
            ShipmentPlusData(
                isShow = isShow,
                text = text,
                action = action,
                actionLink = actionLink,
                logoUrl = logoUrl,
                logoUrlDark = logoUrlDark,
                bgUrl = bgUrl,
                bgUrlDark = bgUrlDark,
                trackerData = ShipmentPlusData.TrackerData(
                    isPlus = isPlus
                )
            )
        }
    }

    var state: State = Loading

    sealed interface State

    object Loading : State

    data class Success(
        val logo: String,
        val logoHeight: Int,
        val title: String,
        val slashPrice: String,
        val background: String,
        val body: List<Info>,

        val tickers: List<TickerData>,
        val tips: Tips?,
        val shipmentPlus: ShipmentPlusData,

        // additional data from old shipment
        val labels: List<String>,
        val isCod: Boolean,
        val isScheduled: Boolean

    ) : State

    data class Error(
        val title: String,
        val subtitle: String,
        val background: String,
        val errorCode: Int
    ) : State

    object Failed : State

    data class Info(
        val logo: Int,
        val text: String
    )

    object Ticker {
        private const val TYPE_INFO = "info"
        private const val TYPE_WARNING = "warning"

        val types = listOf(
            TYPE_INFO,
            TYPE_WARNING
        )

        fun mapTickerType(type: String): Int = when (type) {
            TYPE_INFO -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ANNOUNCEMENT
            TYPE_WARNING -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_WARNING
            else -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ANNOUNCEMENT
        }
    }

    data class Tips(
        val title: String,
        val message: String,
        val link: String,
        val action: String
    ) {
        companion object {
            private const val TYPE_TIPS = "tips"

            val types = listOf(
                TYPE_TIPS
            )
        }
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
}
