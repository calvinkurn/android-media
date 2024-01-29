package com.tokopedia.recommendation_widget_common.widget.foryou.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouFeedBannerBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.topads.sdk.R as topadssdkR

class BannerRecommendationViewHolder constructor(
    view: View,
    private val listener: Listener
) : BaseForYouViewHolder<BannerRecommendationModel>(
    view,
    BannerRecommendationModel::class.java
) {

    private val binding = WidgetForYouFeedBannerBinding.bind(view)

    override fun bind(element: BannerRecommendationModel) {
        setBannerImageUrl(element.imageUrl)
        setBannerOnClickListener(element)
        setBannerImpression(element)
    }

    private fun setBannerOnClickListener(element: BannerRecommendationModel) {
        binding.bannerImageView.setOnClickListener {
            listener.onBannerClick(element)
        }
    }

    private fun setBannerImpression(
        element: BannerRecommendationModel
    ) {
        binding.bannerImageView.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onBannerImpression(element)
                }
            }
        )
    }

    private fun setBannerImageUrl(imageUrl: String) {
        Glide.with(binding.root.context)
            .asBitmap()
            .load(imageUrl)
            .dontAnimate()
            .placeholder(topadssdkR.drawable.loading_page)
            .error(topadssdkR.drawable.error_drawable)
            .into(binding.bannerImageView)
    }

    interface Listener {
        fun onBannerClick(model: BannerRecommendationModel)
        fun onBannerImpression(model: BannerRecommendationModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_feed_banner
    }
}
