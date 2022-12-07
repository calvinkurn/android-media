package com.tokopedia.tokopedianow.search.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateNoResultViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowEmptyStateOocViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.TokoNowFeedbackWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductRecommendationOocViewHolder
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
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.SwitcherWidgetListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder

class SearchTypeFactoryImpl(
    tokoNowEmptyStateOocListener: TokoNowEmptyStateOocViewHolder.TokoNowEmptyStateOocListener,
    chooseAddressListener: ChooseAddressListener,
    titleListener: TitleListener,
    bannerListener: BannerComponentListener,
    quickFilterListener: QuickFilterListener,
    categoryFilterListener: CategoryFilterListener,
    productItemListener: ProductItemListener,
    similarProductListener: SimilarProductListener,
    switcherWidgetListener: SwitcherWidgetListener,
    tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultViewHolder.TokoNowEmptyStateNoResultListener,
    productRecommendationBindOocListener: TokoNowProductRecommendationOocViewHolder.TokonowRecomBindPageNameListener,
    productRecommendationOocListener: TokoNowProductRecommendationOocViewHolder.TokoNowRecommendationCarouselListener,
    productRecommendationListener: TokoNowProductRecommendationView.TokoNowProductRecommendationListener?,
    private val suggestionListener: SuggestionListener,
    private val categoryJumperListener: CategoryJumperListener,
    private val ctaTokoNowHomeListener: CTATokoNowHomeListener,
    private val broadMatchListener: BroadMatchListener,
    feedbackWidgetListener: TokoNowFeedbackWidgetViewHolder.FeedbackWidgetListener
): BaseSearchCategoryTypeFactoryImpl(
    tokoNowEmptyStateOocListener,
    chooseAddressListener,
    titleListener,
    bannerListener,
    quickFilterListener,
    categoryFilterListener,
    productItemListener,
    switcherWidgetListener,
    tokoNowEmptyStateNoResultListener,
    feedbackWidgetListener,
    similarProductListener,
    productRecommendationBindOocListener,
    productRecommendationOocListener,
    productRecommendationListener
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
