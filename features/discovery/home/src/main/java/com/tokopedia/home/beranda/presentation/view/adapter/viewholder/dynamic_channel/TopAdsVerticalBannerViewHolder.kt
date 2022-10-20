package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsVerticalBannerDataModel
import com.tokopedia.home.databinding.ItemHomeVerticalTopAdsBannerBinding
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.utils.view.binding.viewBinding

class TopAdsVerticalBannerViewHolder constructor(
    itemView: View?
) : AbstractViewHolder<HomeTopAdsVerticalBannerDataModel>(itemView) {

    private var binding: ItemHomeVerticalTopAdsBannerBinding? by viewBinding()

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
            binding?.topadsBanner?.renderTdnBanner(tdnBannerList.first(), onTdnBannerClicked = {
                if (it.isNotEmpty()) RouteManager.route(binding?.topadsBanner?.context, it)
            })
        }
    }

    companion object {
        val LAYOUT = R.layout.item_home_vertical_top_ads_banner
    }
}
