package com.tokopedia.recommendation_widget_common.widget.productcard.carousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselBannerDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselProductCardDataModel
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model.RecomCarouselSeeMoreDataModel

/**
 * Created by yfsx on 5/3/21.
 */
interface CommonRecomCarouselCardTypeFactory: AdapterTypeFactory {
    fun type(dataModel: RecomCarouselProductCardDataModel): Int
    fun type(bannerModel: RecomCarouselBannerDataModel): Int
    fun type(seeMoreModel: RecomCarouselSeeMoreDataModel): Int
//    fun type(dataModel: CarouselFeaturedShopCardDataModel): Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}