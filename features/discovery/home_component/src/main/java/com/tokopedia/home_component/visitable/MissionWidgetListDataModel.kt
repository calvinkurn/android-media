package com.tokopedia.home_component.visitable

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by dhaba
 */
data class MissionWidgetListDataModel(
    val missionWidgetList: List<MissionWidgetDataModel> = listOf(),
    val channelModel: ChannelModel,
    val status: Int,
    val subtitleHeight: Int = 0
) : HomeComponentVisitable, ImpressHolder() {

    companion object {
        private const val MAX_MISSION_WIDGET_SIZE = 6
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_EMPTY = 3
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