package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.databinding.HomeFeedBannerBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.R as topadssdkR

class HomeBannerFeedViewHolder(
    itemView: View,
    private val homeRecommendationListener: GlobalRecomListener
) : BaseRecommendationViewHolder<BannerRecommendationDataModel>(
    itemView,
    BannerRecommendationDataModel::class.java
) {

    private val binding = HomeFeedBannerBinding.bind(itemView)

    override fun bind(element: BannerRecommendationDataModel) {
        setBannerImageUrl(element.imageUrl)
        setBannerOnClickListener(element)
        setBannerImpression(element)
    }

    private fun setBannerOnClickListener(element: BannerRecommendationDataModel) {
        binding.bannerImageView.setOnClickListener {
            HomePageTracking.eventClickOnBannerFeed(
                element.toModel(),
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
                    homeRecommendationListener.onBannerImpressed(element.toModel())
                }
            }
        )
    }

    private fun setBannerImageUrl(
        imageUrl: String
    ) {
        binding.bannerImageView.loadImage(imageUrl) {
            setPlaceHolder(R.drawable.loading_page)
            setErrorDrawable(R.drawable.error_drawable)
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.home_feed_banner
    }
}
