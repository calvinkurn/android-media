package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel

/**
 * Created by yfsx on 5/3/21.
 */
class RecomCarouselBannerViewHolder(view: View,
                                    private val data: RecommendationWidget)
    : AbstractViewHolder<RecomCarouselSeeMoreDataModel>(view) {

    private val container: View by lazy { view.findViewById<View>(R.id.container_banner) }
    private val bannerImage: ImageView by lazy { view.findViewById<ImageView>(R.id.image_banner) }

    override fun bind(element: RecomCarouselSeeMoreDataModel) {
        bannerImage.loadImage(element.backgroundImage)
        bannerImage.setOnClickListener {
            element.listener?.onSeeMoreCardClicked(applink = element.applink, data = data)
        }
        container.setOnClickListener {
            element.listener?.onSeeMoreCardClicked(applink = element.applink, data = data)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_recom_carousel_banner
    }
}