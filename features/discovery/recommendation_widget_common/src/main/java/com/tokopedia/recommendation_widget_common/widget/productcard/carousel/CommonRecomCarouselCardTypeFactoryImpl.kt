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
class CommonRecomCarouselCardTypeFactoryImpl constructor(
    private val data: RecommendationWidget,
    private val forceUseOldProductCard: Boolean
) : BaseAdapterTypeFactory(), CommonRecomCarouselCardTypeFactory {

    override fun type(dataModel: RecomCarouselProductCardDataModel): Int {
        return if (dataModel.recomItem.isUseQuantityEditor() && forceUseOldProductCard) {
            RecomCarouselProductCardViewHolder.LAYOUT_V4
        } else {
            RecomCarouselProductCardViewHolder.LAYOUT_V5
        }
    }

    override fun type(bannerModel: RecomCarouselBannerDataModel): Int {
        return RecomCarouselBannerViewHolder.LAYOUT
    }

    override fun type(seeMoreModel: RecomCarouselSeeMoreDataModel): Int {
        return RecomCarouselSeeMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            RecomCarouselProductCardViewHolder.LAYOUT_V4 -> RecomCarouselProductCardViewHolder(parent, data)
            RecomCarouselProductCardViewHolder.LAYOUT_V5 -> RecomCarouselProductCardViewHolder(parent, data)
            RecomCarouselSeeMoreViewHolder.LAYOUT -> RecomCarouselSeeMoreViewHolder(parent, data)
            RecomCarouselBannerViewHolder.LAYOUT -> RecomCarouselBannerViewHolder(parent, data)
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}
