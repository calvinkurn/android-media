package com.tokopedia.seller.search.feature.suggestion.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.HighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.DividerSellerSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.TitleHasMoreSellerSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.TitleHeaderSellerSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.faq.ItemFaqSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights.HighlightSuggestionSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights.TitleHighlightSuggestionSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.navigation.ItemNavigationSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.order.ItemOrderSearchViewHolder
import com.tokopedia.seller.search.feature.suggestion.view.viewholder.product.ItemProductSearchViewHolder

class SuggestionSearchAdapterTypeFactory(
        private val orderSearchListener: OrderSearchListener,
        private val productSearchListener: ProductSearchListener,
        private val navigationSearchListener: NavigationSearchListener,
        private val faqSearchListener: FaqSearchListener,
        private val highlightSuggestionSearchListener: HighlightSuggestionSearchListener
) : BaseAdapterTypeFactory(), TypeFactorySuggestionSearchAdapter {

    override fun type(loadingModel: LoadingSearchModel): Int {
        return ShimmerLoadingViewHolder.LAYOUT
    }

    override fun type(itemTitleHighlightSuggestionSearchUiModel: ItemTitleHighlightSuggestionSearchUiModel): Int {
        return TitleHighlightSuggestionSearchViewHolder.LAYOUT
    }

    override fun type(highlightSuggestionSearchUiModel: HighlightSuggestionSearchUiModel): Int {
        return HighlightSuggestionSearchViewHolder.LAYOUT
    }

    override fun type(sellerSearchNoResultUiModel: SellerSearchNoResultUiModel): Int {
        return SellerSearchNoResultViewHolder.LAYOUT
    }

    override fun type(titleHeaderSellerSearchUiModel: TitleHeaderSellerSearchUiModel): Int {
        return TitleHeaderSellerSearchViewHolder.LAYOUT
    }

    override fun type(titleHasMoreSellerSearchUiModel: TitleHasMoreSellerSearchUiModel): Int {
        return TitleHasMoreSellerSearchViewHolder.LAYOUT
    }

    override fun type(dividerSellerSearchUiModel: DividerSellerSearchUiModel): Int {
        return DividerSellerSearchViewHolder.LAYOUT
    }

    override fun type(orderSellerSearchUiModel: OrderSellerSearchUiModel): Int {
        return ItemOrderSearchViewHolder.LAYOUT
    }

    override fun type(productSellerSearchUiModel: ProductSellerSearchUiModel): Int {
        return ItemProductSearchViewHolder.LAYOUT
    }

    override fun type(navigationSellerSearchUiModel: NavigationSellerSearchUiModel): Int {
        return ItemNavigationSearchViewHolder.LAYOUT
    }

    override fun type(faqSellerSearchUiModel: FaqSellerSearchUiModel): Int {
        return ItemFaqSearchViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TitleHeaderSellerSearchViewHolder.LAYOUT -> TitleHeaderSellerSearchViewHolder(parent)
            TitleHasMoreSellerSearchViewHolder.LAYOUT ->
                TitleHasMoreSellerSearchViewHolder(parent, orderSearchListener, productSearchListener, faqSearchListener)
            DividerSellerSearchViewHolder.LAYOUT -> DividerSellerSearchViewHolder(parent)
            ItemOrderSearchViewHolder.LAYOUT -> ItemOrderSearchViewHolder(parent, orderSearchListener)
            ItemProductSearchViewHolder.LAYOUT -> ItemProductSearchViewHolder(parent, productSearchListener)
            ItemNavigationSearchViewHolder.LAYOUT -> ItemNavigationSearchViewHolder(parent, navigationSearchListener)
            ItemFaqSearchViewHolder.LAYOUT -> ItemFaqSearchViewHolder(parent, faqSearchListener)
            ShimmerLoadingViewHolder.LAYOUT -> ShimmerLoadingViewHolder(parent)
            SellerSearchNoResultViewHolder.LAYOUT -> SellerSearchNoResultViewHolder(parent)
            TitleHighlightSuggestionSearchViewHolder.LAYOUT -> TitleHighlightSuggestionSearchViewHolder(parent)
            HighlightSuggestionSearchViewHolder.LAYOUT -> HighlightSuggestionSearchViewHolder(parent, highlightSuggestionSearchListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}