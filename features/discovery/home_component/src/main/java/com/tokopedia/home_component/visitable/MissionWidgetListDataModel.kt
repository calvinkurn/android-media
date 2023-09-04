package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class MissionWidgetListDataModel(
    val missionWidgetList: List<MissionWidgetDataModel> = listOf(),
    val channelModel: ChannelModel,
    val status: Int = STATUS_LOADING
) : HomeComponentVisitable,
    ImpressHolder(),
    LoadableComponent by BlocksLoadableComponent(
        { status != STATUS_LOADING },
        "MissionWidgetListDataModel"
    ) {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
    }

    fun isShowMissionWidget(): Boolean {
        return if (status == STATUS_SUCCESS) missionWidgetList.isNotEmpty() else true
    }

    override fun visitableId(): String {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return this === b
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle.EMPTY
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
