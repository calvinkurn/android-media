package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.viewholder

import android.view.View
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.DtHomeRecommedationHeadlineAdsLayoutBinding
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationHeadlineTopAdsViewHolder(view: View, private val topAdsBannerClickListener: TopAdsBannerClickListener) :
    SmartAbstractViewHolder<HomeRecommendationHeadlineTopAdsDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.dt_home_recommedation_headline_ads_layout
    }

    private var binding: DtHomeRecommedationHeadlineAdsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationHeadlineTopAdsDataModel, listener: SmartListener) {
        binding?.headlineAds?.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                topAdsBannerClickListener.onBannerAdsClicked(
                    position,
                    applink,
                    data
                )
            }
        })
        binding?.headlineAds?.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                // no-op
            }
        })
        if (!element.headlineAds.data.isNullOrEmpty()) {
            binding?.headlineAds?.displayAds(element.headlineAds, Int.ZERO)
        }
        binding?.headlineAdsShimmer?.hide()
    }
}
