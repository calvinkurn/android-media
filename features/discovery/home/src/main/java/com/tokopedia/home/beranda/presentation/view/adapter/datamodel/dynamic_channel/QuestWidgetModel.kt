package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.quest_widget.data.QuestData

data class QuestWidgetModel(
    val channelModel: ChannelModel,
    val pageName: String="",
    val questData : QuestData = QuestData(),
    private val isCache: Boolean = false
): HomeVisitable {
    override fun visitableId(): String? {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is QuestWidgetModel) {
            channelModel.channelConfig.createdTimeMillis == b.channelModel.channelConfig.createdTimeMillis
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return  mutableMapOf()
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
}