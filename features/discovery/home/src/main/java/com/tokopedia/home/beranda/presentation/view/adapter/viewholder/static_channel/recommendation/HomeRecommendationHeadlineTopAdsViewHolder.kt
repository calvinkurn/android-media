package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.home.databinding.HomeRecommedationHeadlineAdsLayoutBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationHeadlineTopAdsViewHolder(
    view: View,
    private val topAdsBannerClickListener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationHeadlineTopAdsDataModel>(
    view,
    HomeRecommendationHeadlineTopAdsDataModel::class.java
) {
    companion object {
        val LAYOUT = R.layout.home_recommedation_headline_ads_layout
    }

    private var binding: HomeRecommedationHeadlineAdsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationHeadlineTopAdsDataModel) {
        setHeadlineAdsClickListener()
        setHeadlineAdsImpression()
        setDisplayHeadlineAds(element)
        hideHeadlineAdsShimmer()
    }

    private fun setHeadlineAdsImpression() {
        binding?.headlineAds?.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
            }
        })
    }

    private fun setDisplayHeadlineAds(
        element: HomeRecommendationHeadlineTopAdsDataModel
    ) {
        if (!element.headlineAds.data.isNullOrEmpty()) {
            binding?.headlineAds?.displayAds(element.headlineAds, Int.ZERO)
        }
    }

    private fun hideHeadlineAdsShimmer() {
        binding?.headlineAdsShimmer?.hide()
    }

    private fun setHeadlineAdsClickListener() {
        binding?.headlineAds?.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                topAdsBannerClickListener.onBannerAdsClicked(
                    position,
                    applink,
                    data
                )
            }
        })
    }
}
