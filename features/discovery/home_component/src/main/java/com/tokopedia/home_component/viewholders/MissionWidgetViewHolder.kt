package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel

/**
 * Created by dhaba
 */
class MissionWidgetViewHolder(
    itemView: View,
    val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val cardInteraction: Boolean = false) : AbstractViewHolder<MissionWidgetListDataModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.global_component_mission_widget
    }

    override fun bind(element: MissionWidgetListDataModel?) {

    }
}