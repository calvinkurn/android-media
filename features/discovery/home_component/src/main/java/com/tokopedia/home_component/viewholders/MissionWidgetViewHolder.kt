package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MissionWidgetComponentListener

/**
 * Created by dhaba
 */
class MissionWidgetViewHolder(
    itemView: View,
    val homeComponentListener: HomeComponentListener?,
    val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val cardInteraction: Boolean = false) {
    companion object {
        val LAYOUT = R.layout.global_component_mission_widget
    }
}