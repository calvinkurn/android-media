package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.util.ChannelStyleUtil.parseWithSubtitle
import com.tokopedia.home_component.util.HomeComponentFeatureFlag
import com.tokopedia.home_component.util.MissionWidgetCardUtil
import com.tokopedia.home_component.util.MissionWidgetClearUtil
import com.tokopedia.home_component.util.MissionWidgetUtil
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class MissionWidgetListDataModel(
    val id: String = "",
    val name: String = "",
    val header: ChannelHeader = ChannelHeader(),
    val config: ChannelConfig = ChannelConfig(),
    val widgetParam: String = "",
    val missionWidgetList: List<MissionWidgetDataModel> = listOf(),
    val verticalPosition: Int = 0,
    val status: Int = STATUS_LOADING,
    val showShimmering: Boolean = true,
    val source: Int,
    val type: Type = Type.CARD,
) : HomeComponentVisitable, ImpressHolder(),
    LoadableComponent by BlocksLoadableComponent(
        { status != STATUS_LOADING },
        "MissionWidgetListDataModel"
    ) {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
        const val SOURCE_ATF = 0
        const val SOURCE_DC = 1
        const val PAYLOAD_IS_REFRESH = "isRefresh"
    }

    val missionWidgetUtil: MissionWidgetUtil = when(type) {
        Type.CLEAR -> MissionWidgetClearUtil()
        else -> MissionWidgetCardUtil()
    }

    fun isShowMissionWidget(): Boolean {
        return when(status) {
            STATUS_SUCCESS -> missionWidgetList.isNotEmpty()
            STATUS_LOADING -> showShimmering
            else -> true
        }
    }

    fun isWithSubtitle(): Boolean {
        return HomeComponentFeatureFlag.isMissionExpVariant() || config.styleParam.parseWithSubtitle()
    }

    override fun visitableId(): String {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return this == b
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        val bundle = Bundle()
        if(isRefresh(b)) {
            bundle.putBoolean(PAYLOAD_IS_REFRESH, true)
        }
        return bundle
    }

    private fun isRefresh(b: Any?): Boolean {
        return b is MissionWidgetListDataModel
            && this.status != STATUS_LOADING
            && b.status == STATUS_LOADING
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

    enum class Type {
        CARD,
        CLEAR
    }
}
