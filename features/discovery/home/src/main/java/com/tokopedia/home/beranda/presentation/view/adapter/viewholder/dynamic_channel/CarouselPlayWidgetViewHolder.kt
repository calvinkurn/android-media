package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home.databinding.HomeDcPlayBannerCarouselBinding
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetHomeAnalyticModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by mzennis on 19/10/20.
 */
class CarouselPlayWidgetViewHolder(
    private val view: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    val homeCategoryListener: HomeCategoryListener
) : AbstractViewHolder<CarouselPlayWidgetDataModel>(view) {

    private var binding: HomeDcPlayBannerCarouselBinding? by viewBinding()

    init {
        playWidgetViewHolder.coordinator.setAnalyticModel(PlayWidgetHomeAnalyticModel())
    }

    override fun bind(element: CarouselPlayWidgetDataModel?) {
        element?.let {
            playWidgetViewHolder.bind(element.widgetState, this)
            setChannelDivider(element)
        }
    }

    override fun bind(element: CarouselPlayWidgetDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: CarouselPlayWidgetDataModel) {
        HomeChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.homeChannel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.home_dc_play_banner_carousel
    }
}