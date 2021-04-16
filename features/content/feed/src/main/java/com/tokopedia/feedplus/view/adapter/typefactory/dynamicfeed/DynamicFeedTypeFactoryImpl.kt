package com.tokopedia.feedplus.view.adapter.typefactory.dynamicfeed

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView

/**
 * @author by yoasfs on 2019-08-06
 */
class DynamicFeedTypeFactoryImpl(val highlightListener: HighlightAdapter.HighlightListener, val cardTitleListener: CardTitleView.CardTitleListener)
    : BaseAdapterTypeFactory(), DynamicFeedTypeFactory {

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return 0
    }

    override fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int {
        return 0
    }

    override fun type(bannerViewModel: BannerViewModel): Int {
        return 0
    }

    override fun type(topadsShopUiModel: TopadsShopUiModel): Int {
        return 0
    }

    override fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int {
        return 0
    }

    override fun type(highlightViewModel: HighlightViewModel): Int {
        return HighlightViewHolder.LAYOUT
    }

    override fun type(topAdsBannerViewmodel: TopAdsBannerViewModel): Int {
        return TopAdsBannerViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardViewModel: CarouselPlayCardViewModel): Int {
        return 0
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            HighlightViewHolder.LAYOUT ->
                HighlightViewHolder(parent, highlightListener, cardTitleListener ) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)

        }
    }
}