package com.tokopedia.seller.search.feature.suggestion.view.model.compose

import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchSubItemUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel

sealed class SuggestionSearchUiEvent {

    object OnSellerSearchNoResult : SuggestionSearchUiEvent()

    data class OnArticleItemClicked(
        val articleSellerSearchUiModel: ArticleSellerSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    data class OnFaqItemClicked(
        val faqSearchUiModel: FaqSellerSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    data class OnHighlightItemClicked(
        val itemHighlightSuggestionSearchUiModel: ItemHighlightSuggestionSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    data class OnNavigationItemClicked(
        val navigationSellerSearchUiModel: NavigationSellerSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    data class OnNavigationSellerSearchSubItemClicked(
        val navigationSellerSearchSubItemUiModel: NavigationSellerSearchSubItemUiModel
    ) : SuggestionSearchUiEvent()

    data class OnOrderItemClicked(
        val orderSellerSearchUiModel: OrderSellerSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    data class OnProductItemClicked(
        val productSellerSearchUiModel: ProductSellerSearchUiModel,
        val position: Int
    ) : SuggestionSearchUiEvent()

    abstract class OnSeeMoreClicked(
        open val item: TitleHasMoreSellerSearchUiModel
    ) : SuggestionSearchUiEvent()

    data class OnOrderMoreClicked(
        override val item: TitleHasMoreSellerSearchUiModel
    ) : OnSeeMoreClicked(item)

    data class OnProductMoreClicked(
        override val item: TitleHasMoreSellerSearchUiModel
    ) : OnSeeMoreClicked(item)

    data class OnFaqMoreClicked(
        override val item: TitleHasMoreSellerSearchUiModel
    ) : OnSeeMoreClicked(item)

    data class OnArticleMoreClicked(
        override val item: TitleHasMoreSellerSearchUiModel
    ) : OnSeeMoreClicked(item)
}
