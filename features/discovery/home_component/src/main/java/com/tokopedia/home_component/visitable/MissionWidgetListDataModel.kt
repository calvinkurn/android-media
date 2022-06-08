package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class MissionWidgetListDataModel(
    val missionWidgetList: List<MissionWidgetDataModel> = listOf(),
    val channelModel: ChannelModel,
    val isErrorLoad: Boolean = false
) : HomeComponentVisitable, ImpressHolder() {

    companion object {
        private const val MAX_MISSION_WIDGET_SIZE = 6
    }

    fun isShowMissionWidget() =
        missionWidgetList.isNotEmpty() && missionWidgetList.size <= MAX_MISSION_WIDGET_SIZE

    override fun visitableId(): String {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is MerchantVoucherDataModel) {
            channelModel.channelConfig.createdTimeMillis == b.channelModel.channelConfig.createdTimeMillis
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

}