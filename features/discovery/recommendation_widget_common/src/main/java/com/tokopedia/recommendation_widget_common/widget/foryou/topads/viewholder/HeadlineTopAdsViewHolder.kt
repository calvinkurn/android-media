package com.tokopedia.recommendation_widget_common.widget.foryou.topads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouHeadlineTopAdsBinding
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsListener
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.HeadlineTopAdsModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class HeadlineTopAdsViewHolder constructor(
    view: View,
    private val listener: BannerTopAdsListener
) : BaseForYouViewHolder<HeadlineTopAdsModel>(
    view,
    HeadlineTopAdsModel::class.java
) {

    private val binding: WidgetForYouHeadlineTopAdsBinding? by viewBinding()

    override fun bind(element: HeadlineTopAdsModel) {
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

    private fun setDisplayHeadlineAds(element: HeadlineTopAdsModel) {
        if (element.headlineAds.data.isNotEmpty()) {
            binding?.headlineAds?.displayAds(element.headlineAds, Int.ZERO)
        }
    }

    private fun hideHeadlineAdsShimmer() {
        binding?.headlineAdsShimmer?.hide()
    }

    private fun setHeadlineAdsClickListener() {
        binding?.headlineAds?.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                listener.onBannerAdsClicked(
                    position,
                    applink,
                    data
                )
            }
        })
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_headline_top_ads
    }
}
