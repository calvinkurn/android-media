package com.tokopedia.recommendation_widget_common.infinite.foryou.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouBannerBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.TemporaryBackwardCompatible
import com.tokopedia.utils.view.binding.viewBinding

@TemporaryBackwardCompatible
class BannerRecommendationViewHolder(
    view: View,
    private val listener: GlobalRecomListener
) : BaseRecommendationViewHolder<BannerRecommendationModel>(
    view,
    BannerRecommendationModel::class.java
) {

    private val binding: WidgetForYouBannerBinding? by viewBinding()

    override fun bind(element: BannerRecommendationModel) {
        binding?.bannerImageView?.loadImage(element.imageUrl)
        setBannerOnClickListener(element)
        setBannerImpression(element)
    }

    private fun setBannerOnClickListener(element: BannerRecommendationModel) {
        binding?.bannerImageView?.setOnClickListener {
            listener.onBannerClicked(element)
        }
    }

    private fun setBannerImpression(element: BannerRecommendationModel) {
        binding?.bannerImageView?.addOnImpressionListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onBannerImpressed(element)
                }
            }
        )
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_banner
    }
}
