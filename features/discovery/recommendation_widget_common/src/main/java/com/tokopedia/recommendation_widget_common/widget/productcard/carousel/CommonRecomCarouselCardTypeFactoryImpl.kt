package com.tokopedia.recommendation_widget_common.widget.productcard.carousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselProductCardDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder.RecomCarouselBannerViewHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder.RecomCarouselProductCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.viewholder.RecomCarouselSeeMoreViewHolder

/**
 * Created by yfsx on 5/3/21.
 */
class CommonRecomCarouselCardTypeFactoryImpl (private val data: RecommendationWidget) :
        BaseAdapterTypeFactory(), CommonRecomCarouselCardTypeFactory {

    override fun type(dataModel: RecomCarouselProductCardDataModel): Int {
        return RecomCarouselProductCardViewHolder.LAYOUT
    }

    override fun type(bannerModel: RecomCarouselBannerDataModel): Int {
        return RecomCarouselBannerViewHolder.LAYOUT
    }

    override fun type(seeMoreModel: RecomCarouselSeeMoreDataModel): Int {
        return RecomCarouselSeeMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            RecomCarouselProductCardViewHolder.LAYOUT -> {
                RecomCarouselProductCardViewHolder(parent, data)
            }
            RecomCarouselSeeMoreViewHolder.LAYOUT -> {
                RecomCarouselSeeMoreViewHolder(parent, data)
            }
            RecomCarouselBannerViewHolder.LAYOUT -> {
                RecomCarouselBannerViewHolder(parent, data)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}