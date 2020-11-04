package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase.WidgetSource
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePerso
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class RechargeBUWidgetDataModel(
        val data: RechargePerso = RechargePerso(),
        val source: WidgetSource,
        var isDataCache: Boolean = false
): ImpressHolder(), HomeVisitable {

    override fun isTrackingCombined(): Boolean = false

    override fun isCache(): Boolean = isDataCache

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {

    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeBUWidgetDataModel) {
            data.items == b.data.items
        } else false
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return mutableListOf()
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    override fun setTrackingCombined(isCombined: Boolean) {

    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        return Bundle()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {

    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return data.title
    }
}