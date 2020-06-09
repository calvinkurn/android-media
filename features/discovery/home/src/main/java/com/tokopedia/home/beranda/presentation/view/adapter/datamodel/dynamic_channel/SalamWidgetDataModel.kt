package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

data class SalamWidgetDataModel(
        var salamWidget : SalamWidget = SalamWidget()
) : ImpressHolder(), HomeVisitable {
    override fun equalsWith(b: Any?): Boolean {
        if (b is SalamWidgetDataModel) {
            return salamWidget == b.salamWidget
        }
        return false
    }
    override fun visitableId(): String {
        return salamWidget.salamWidget.iD.toString()
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return mutableListOf()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {
    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {
    }

    override fun isCache(): Boolean {
        return false
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }
}