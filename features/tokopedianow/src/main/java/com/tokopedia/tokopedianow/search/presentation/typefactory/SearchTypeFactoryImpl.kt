package com.tokopedia.tokopedianow.search.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowRecommendationCarouselViewHolder
import com.tokopedia.tokopedianow.search.presentation.listener.BroadMatchListener
import com.tokopedia.tokopedianow.search.presentation.listener.CTATokoNowHomeListener
import com.tokopedia.tokopedianow.search.presentation.listener.CategoryJumperListener
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.CTATokopediaNowHomeDataView
import com.tokopedia.tokopedianow.search.presentation.model.CategoryJumperDataView
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.viewholder.BroadMatchViewHolder
import com.tokopedia.tokopedianow.search.presentation.viewholder.CTATokopediaNowHomeViewHolder
import com.tokopedia.tokopedianow.search.presentation.viewholder.SearchCategoryJumperViewHolder
import com.tokopedia.tokopedianow.search.presentation.viewholder.SearchChooseAddressViewHolder
import com.tokopedia.tokopedianow.search.presentation.viewholder.SuggestionViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.EmptyProductListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OutOfCoverageListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SearchCategoryRecommendationCarouselListener
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class SearchTypeFactoryImpl(
        chooseAddressListener: ChooseAddressListener,
        titleListener: TitleListener,
        bannerListener: BannerComponentListener,
        quickFilterListener: QuickFilterListener,
        categoryFilterListener: CategoryFilterListener,
        productItemListener: ProductItemListener,
        emptyProductListener: EmptyProductListener,
        private val suggestionListener: SuggestionListener,
        outOfCoverageListener: OutOfCoverageListener,
        private val categoryJumperListener: CategoryJumperListener,
        private val ctaTokoNowHomeListener: CTATokoNowHomeListener,
        recommendationCarouselListener: TokoNowRecommendationCarouselViewHolder.TokoNowRecommendationCarouselListener,
        private val broadMatchListener: BroadMatchListener,
): BaseSearchCategoryTypeFactoryImpl(
        chooseAddressListener,
        titleListener,
        bannerListener,
        quickFilterListener,
        categoryFilterListener,
        productItemListener,
        emptyProductListener,
        outOfCoverageListener,
        recommendationCarouselListener,
), SearchTypeFactory {

    override fun type(suggestionDataView: SuggestionDataView): Int {
        return SuggestionViewHolder.LAYOUT
    }

    override fun type(categoryJumperDataView: CategoryJumperDataView): Int {
        return SearchCategoryJumperViewHolder.LAYOUT
    }

    override fun type(ctaTokopediaNowHomeDataView: CTATokopediaNowHomeDataView) =
            CTATokopediaNowHomeViewHolder.LAYOUT

    override fun type(broadMatchDataView: BroadMatchDataView) =
        BroadMatchViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BaseChooseAddressViewHolder.LAYOUT -> SearchChooseAddressViewHolder(view, chooseAddressListener)
            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)
            SearchCategoryJumperViewHolder.LAYOUT -> SearchCategoryJumperViewHolder(view, categoryJumperListener)
            CTATokopediaNowHomeViewHolder.LAYOUT -> CTATokopediaNowHomeViewHolder(view, ctaTokoNowHomeListener)
            BroadMatchViewHolder.LAYOUT -> BroadMatchViewHolder(view, broadMatchListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
