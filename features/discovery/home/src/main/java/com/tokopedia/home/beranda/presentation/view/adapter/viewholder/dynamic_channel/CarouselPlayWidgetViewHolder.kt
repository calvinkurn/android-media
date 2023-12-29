package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.listener.CarouselPlayWidgetCallback
import com.tokopedia.home.databinding.HomeDcPlayBannerCarouselBinding
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetHomeAnalyticModel
import com.tokopedia.play.widget.analytic.list.DefaultPlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by mzennis on 19/10/20.
 */
class CarouselPlayWidgetViewHolder(
    view: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val callback: CarouselPlayWidgetCallback,
    private val homeCategoryListener: HomeCategoryListener
) : AbstractViewHolder<CarouselPlayWidgetDataModel>(view) {

    private var binding: HomeDcPlayBannerCarouselBinding? by viewBinding()

    private val playWidgetHomeGlobalAnalyticModel = PlayWidgetHomeAnalyticModel()
    private val carouselAnalyticListener = DefaultPlayWidgetInListAnalyticListener(callback)

    override fun bind(element: CarouselPlayWidgetDataModel?) {
        element?.let {
            callback.setHomeChannelId(element.homeChannel.id)
            callback.setHeaderTitle(element.homeChannel.channelHeader.name)

            val state = it.widgetState
            if (state.widgetType == PlayWidgetType.Carousel) {
                playWidgetViewHolder.coordinator.setAnalyticListener(carouselAnalyticListener)
            } else {
                playWidgetViewHolder.coordinator.setAnalyticModel(playWidgetHomeGlobalAnalyticModel)
            }

            playWidgetViewHolder.bind(element.widgetState, this)
            setChannelDivider(element)
            setChannelHeader(element)
        }
    }

    override fun bind(element: CarouselPlayWidgetDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: CarouselPlayWidgetDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.homeChannel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setChannelHeader(element: CarouselPlayWidgetDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.homeChannel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    // add click view all tracker
                    callback.onClickChevron(verticalWidgetPosition = absoluteAdapterPosition)
                    homeCategoryListener.onDynamicChannelClicked(link)
                }
            }
        )
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.home_dc_play_banner_carousel
    }
}
