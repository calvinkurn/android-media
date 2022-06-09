package com.tokopedia.home_component.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalComponentMissionWidgetBinding
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class MissionWidgetViewHolder(
    itemView: View,
    val missionWidgetComponentListener: MissionWidgetComponentListener,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<MissionWidgetListDataModel>(itemView),
    CommonProductCardCarouselListener {

    companion object {
        val LAYOUT = R.layout.global_component_mission_widget
    }

    private var binding : GlobalComponentMissionWidgetBinding? by viewBinding()

    private fun setHeaderComponent(element: MissionWidgetListDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }

            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }

    override fun bind(element: MissionWidgetListDataModel) {

    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {

    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {

    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {

    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {

    }
}