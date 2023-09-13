package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelConfig
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
    val missionWidgetList: List<MissionWidgetDataModel> = listOf(),
    val verticalPosition: Int = 0,
    val status: Int = STATUS_LOADING,
    val showShimmering: Boolean = true,
) : HomeComponentVisitable, ImpressHolder() {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
    }

    fun isShowMissionWidget(): Boolean {
        return when(status) {
            STATUS_SUCCESS -> missionWidgetList.isNotEmpty()
            STATUS_LOADING -> showShimmering
            else -> true
        }
    }

    override fun visitableId(): String {
        return id
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
