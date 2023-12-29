package com.tokopedia.dilayanitokopedia.ui.home.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.common.view.adapter.typefactory.HomeTypeFactory
import com.tokopedia.dilayanitokopedia.ui.home.adapter.listener.DtHomeCategoryListener
import com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel.HomeLoadingMoreModel
import com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.ui.home.adapter.viewholder.HomeLoadingMoreViewHolder
import com.tokopedia.dilayanitokopedia.ui.home.adapter.viewholder.HomeLoadingStateViewHolder
import com.tokopedia.dilayanitokopedia.ui.home.adapter.viewholder.HomeRecommendationFeedViewHolder
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLoadingStateUiModel
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel

/**
 * Created by irpan on 12/09/22.
 */
class DtHomeAdapterTypeFactory(
    private val homeRecommendationFeedListener: DtHomeCategoryListener,
    private val bannerComponentListener: BannerComponentListener? = null,
    private val dynamicLegoBannerCallback: DynamicLegoBannerListener? = null,
    private val homeTopComponentListener: HomeComponentListener? = null,
    private val homeTopCarouselListener: MixTopComponentListener? = null,
    private val homeLeftCarouselListener: MixLeftComponentListener? = null
) : BaseAdapterTypeFactory(), HomeTypeFactory, HomeComponentTypeFactory {

    // currently used in DT
    override fun type(bannerDataModel: BannerDataModel): Int = BannerComponentViewHolder.LAYOUT
    override fun type(mixTopDataModel: MixTopDataModel): Int = MixTopComponentViewHolder.LAYOUT
    override fun type(mixLeftDataModel: MixLeftDataModel): Int = MixLeftComponentViewHolder.LAYOUT
    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = DynamicLegoBannerViewHolder.LAYOUT

    // loading from FE.
    override fun type(uiModel: HomeLoadingStateUiModel): Int = HomeLoadingStateViewHolder.LAYOUT
    override fun type(uiModel: HomeRecommendationFeedDataModel): Int = HomeRecommendationFeedViewHolder.LAYOUT
    override fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int = HomeLoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLoadingStateViewHolder.LAYOUT -> HomeLoadingStateViewHolder(view)

            // loading more for recommendation feeds.
            HomeLoadingMoreViewHolder.LAYOUT -> HomeLoadingMoreViewHolder(view)

            HomeRecommendationFeedViewHolder.LAYOUT -> {
                HomeRecommendationFeedViewHolder(view, homeRecommendationFeedListener)
            }

            BannerComponentViewHolder.LAYOUT -> {
                BannerComponentViewHolder(view, bannerComponentListener, null)
            }

            DynamicLegoBannerViewHolder.LAYOUT -> {
                DynamicLegoBannerViewHolder(view, dynamicLegoBannerCallback, null)
            }

            MixLeftComponentViewHolder.LAYOUT -> MixLeftComponentViewHolder(view, homeLeftCarouselListener, null)

            MixTopComponentViewHolder.LAYOUT -> MixTopComponentViewHolder(
                view,
                homeTopComponentListener,
                homeTopCarouselListener,
                true
            )

            // endregion
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }
}
