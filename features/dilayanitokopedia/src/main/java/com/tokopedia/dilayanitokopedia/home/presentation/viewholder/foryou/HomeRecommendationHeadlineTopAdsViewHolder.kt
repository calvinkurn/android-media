package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.view.View
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener

class HomeRecommendationHeadlineTopAdsViewHolder(view: View, private val topAdsBannerClickListener: TopAdsBannerClickListener) :
    SmartAbstractViewHolder<HomeRecommendationHeadlineTopAdsDataModel>(view) {
    companion object {
//        val LAYOUT = R.layout.home_recommedation_headline_ads_layout
        val LAYOUT = R.layout.loading_layout
    }

//    private var binding: HomeRecommedationHeadlineAdsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationHeadlineTopAdsDataModel, listener: SmartListener) {
//        binding?.headlineAds?.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
//            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
//                topAdsBannerClickListener.onBannerAdsClicked(
//                    position,
//                    applink,
//                    data
//                )
//            }
//
//        })
//        binding?.headlineAds?.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
//            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
//            }
//        })
//        if (!element.headlineAds.data.isNullOrEmpty()){
//            binding?.headlineAds?.displayAds(element.headlineAds, Int.ZERO)
//        }
//        binding?.headlineAdsShimmer?.hide()
    }
}
