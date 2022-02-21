package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel

/**
 * Created by yfsx on 5/3/21.
 */
class RecomCarouselSeeMoreViewHolder(view: View,
                                     private val data: RecommendationWidget)
    : AbstractViewHolder<RecomCarouselSeeMoreDataModel>(view) {

    private val container: View by lazy { view.findViewById<View>(R.id.container_banner_mix_more) }
    private val bannerBackgroundImage: ImageView by lazy { view.findViewById<ImageView>(R.id.background_banner_mix_more) }

    override fun bind(element: RecomCarouselSeeMoreDataModel) {
        setupListeners(element)
        setupBannerBackgroundImage(element)
    }

    override fun bind(element: RecomCarouselSeeMoreDataModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.containsKey(RecomCarouselSeeMoreDataModel.PAYLOAD_SHOULD_RECREATE_LISTENERS)) {
                setupListeners(element)
            }
            if (payload.containsKey(RecomCarouselSeeMoreDataModel.PAYLOAD_BACKGROUND_IMAGE)) {
                setupBannerBackgroundImage(element)
            }
        }
    }

    private fun setupBannerBackgroundImage(element: RecomCarouselSeeMoreDataModel) {
        if (element.backgroundImage.isNotEmpty()) {
            bannerBackgroundImage.visible()
            bannerBackgroundImage.loadImageWithoutPlaceholder(element.backgroundImage)
        } else {
            bannerBackgroundImage.gone()
        }
    }

    private fun setupListeners(element: RecomCarouselSeeMoreDataModel) {
        bannerBackgroundImage.setOnClickListener {
            element.listener?.onSeeMoreCardClicked(applink = element.applink, data = data)
        }
        container.setOnClickListener {
            element.listener?.onSeeMoreCardClicked(applink = element.applink, data = data)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_recom_carousel_see_more
    }
}