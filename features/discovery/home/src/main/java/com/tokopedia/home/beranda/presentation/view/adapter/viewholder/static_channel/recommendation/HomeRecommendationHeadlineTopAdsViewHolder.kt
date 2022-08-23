package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.home.databinding.HomeRecommedationHeadlineAdsLayoutBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationHeadlineTopAdsViewHolder(view: View, private  val topAdsBannerClickListener: TopAdsBannerClickListener) :
    SmartAbstractViewHolder<HomeRecommendationHeadlineTopAdsDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.home_recommedation_headline_ads_layout
    }

    private var binding: HomeRecommedationHeadlineAdsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationHeadlineTopAdsDataModel, listener: SmartListener) {
        if (!element.headlineAds.data.isNullOrEmpty()){
            binding?.headlineAds?.displayAds(element.headlineAds, Int.ZERO)
        }
        binding?.headlineAdsShimmer?.hide()
        binding?.headlineAds?.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData?) {
            }
        })
        binding?.headlineAds?.setTopAdsBannerClickListener(TopAdsBannerClickListener { position, applink, data ->
            topAdsBannerClickListener.onBannerAdsClicked(
                position,
                applink,
                data
            )
        })
    }
}
