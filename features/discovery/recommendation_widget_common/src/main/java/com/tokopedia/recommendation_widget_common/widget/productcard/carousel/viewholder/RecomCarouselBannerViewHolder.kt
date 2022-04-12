package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel

/**
 * Created by yfsx on 5/3/21.
 */
class RecomCarouselBannerViewHolder(view: View,
                                    private val data: RecommendationWidget)
    : AbstractViewHolder<RecomCarouselBannerDataModel>(view) {

    private val container: View by lazy { view.findViewById<View>(R.id.container_banner) }
    private val bannerImage: ImageView by lazy { view.findViewById<ImageView>(R.id.image_banner) }

    override fun bind(element: RecomCarouselBannerDataModel) {
        setupListeners(element)
        setupBannerImage(element)
        setupBackground(element)
    }

    override fun bind(element: RecomCarouselBannerDataModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.containsKey(RecomCarouselBannerDataModel.PAYLOAD_SHOULD_RECREATE_LISTENERS)) {
                setupListeners(element)
            }
            if (payload.containsKey(RecomCarouselBannerDataModel.PAYLOAD_BANNER_IMAGE)) {
                setupBannerImage(element)
            }
            if (payload.containsKey(RecomCarouselBannerDataModel.PAYLOAD_BACKGROUND_COLOR)) {
                setupBackground(element)
            }
        }
    }

    private fun setupListeners(element: RecomCarouselBannerDataModel) {
        itemView.addOnImpressionListener(element.impressHolder) {
            element.listener?.onBannerCardImpressed(data)
        }
        bannerImage.setOnClickListener {
            element.listener?.onBannerCardClicked(applink = element.applink, data = data)
        }
        container.setOnClickListener {
            element.listener?.onBannerCardClicked(applink = element.applink, data = data)
        }
    }

    private fun setupBannerImage(element: RecomCarouselBannerDataModel) {
        element.bannerImage.let{
            bannerImage.loadImage(it)
        }
    }

    private fun setupBackground(element: RecomCarouselBannerDataModel) {
        if (element.bannerBackgorundColor.isNotEmpty()) {
            container.setBackgroundColor(Color.parseColor(element.bannerBackgorundColor))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_recom_carousel_banner
    }
}