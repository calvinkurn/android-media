package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.gopayhomewidget.presentation.domain.data.PayLaterWidgetData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class HomePayLaterWidgetDataModel (
    var payLaterWidgetData: PayLaterWidgetData?,
    val channel: DynamicHomeChannel.Channels = DynamicHomeChannel.Channels()
) : HomeVisitable, ImpressHolder() {

    private var isCache: Boolean = false
    private var trackingDataForCombination: List<Any> = listOf()
    private var isCombined: Boolean = false
    private var trackingData: MutableMap<String, Any> = mutableMapOf()

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return trackingData
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return trackingDataForCombination.toMutableList()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return channel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return this === b
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        return Bundle.EMPTY
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}