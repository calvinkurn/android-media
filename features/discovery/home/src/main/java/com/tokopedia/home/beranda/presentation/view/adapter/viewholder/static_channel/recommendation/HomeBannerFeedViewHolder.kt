package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.databinding.HomeFeedBannerBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder

class HomeBannerFeedViewHolder(
    itemView: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<BannerRecommendationDataModel>(
    itemView,
    BannerRecommendationDataModel::class.java
) {

    private val binding = HomeFeedBannerBinding.bind(itemView)

    private var item: BannerRecommendationDataModel? = null

    override fun bind(element: BannerRecommendationDataModel) {
        item = element
        setBannerImageUrl(element.imageUrl)
        setBannerOnClickListener(element)
        setBannerImpression(element)
    }

    override fun bindPayload(newItem: BannerRecommendationDataModel?) {
        newItem?.let {
            if (it.imageUrl != item?.imageUrl) {
                setBannerImageUrl(it.imageUrl)
            }
            setBannerOnClickListener(it)
            setBannerImpression(it)
        }
    }

    private fun setBannerOnClickListener(
        element: BannerRecommendationDataModel
    ) {
        binding.bannerImageView.setOnClickListener {
            HomePageTracking.eventClickOnBannerFeed(
                element,
                element.tabName
            )
            RouteManager.route(itemView.context, element.applink)
        }
    }

    private fun setBannerImpression(
        element: BannerRecommendationDataModel
    ) {
        binding.bannerImageView.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    homeRecommendationListener.onBannerImpression(element)
                }
            }
        )
    }

    private fun setBannerImageUrl(
        imageUrl: String
    ) {
        binding.bannerImageView.loadImage(imageUrl)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_feed_banner
    }
}
