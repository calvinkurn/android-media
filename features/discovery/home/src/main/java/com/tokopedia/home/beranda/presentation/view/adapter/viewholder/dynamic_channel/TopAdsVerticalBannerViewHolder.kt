package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsVerticalBannerDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeChannelWidgetUtil
import com.tokopedia.home.databinding.ItemHomeVerticalTopAdsBannerBinding
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.utils.view.binding.viewBinding

class TopAdsVerticalBannerViewHolder constructor(
    itemView: View?
) : AbstractViewHolder<HomeTopAdsVerticalBannerDataModel>(itemView) {

    private var binding: ItemHomeVerticalTopAdsBannerBinding? by viewBinding()

    override fun bind(element: HomeTopAdsVerticalBannerDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(element: HomeTopAdsVerticalBannerDataModel) {

        binding?.dynamicChannelHeader?.setChannel(
            DynamicChannelComponentMapper.mapHomeChannelToComponent(
                element.channel,
                adapterPosition
            ),
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {

                }

                override fun onChannelExpired(channelModel: ChannelModel) {

                }
            }
        )

        val tdnBannerList = element.topAdsImageViewModelList?.let {
            TdnHelper.categoriesTdnBanners(
                it
            )
        }

        if (!tdnBannerList.isNullOrEmpty()) {
            binding?.root?.show()
            itemView.show()
            binding?.topadsBanner?.renderTdnBanner(tdnBannerList.first(), onTdnBannerClicked = {
                if (it.isNotEmpty()) RouteManager.route(binding?.topadsBanner?.context, it)
            })
            setChannelDivider(element.channel)
        } else {
            binding?.root?.gone()
            itemView.gone()
        }
    }

    private fun setChannelDivider(channel: DynamicHomeChannel.Channels) {
        binding?.run {
            HomeChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channel,
                dividerTop = homeComponentTopAdsDividerHeader,
                dividerBottom = homeComponentTopAdsDividerFooter
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_home_vertical_top_ads_banner
    }
}
