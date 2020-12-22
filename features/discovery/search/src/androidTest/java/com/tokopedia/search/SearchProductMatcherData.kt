package com.tokopedia.search

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.*

internal fun List<Visitable<*>>.getGlobalNavViewModelPosition(): Int {
    return indexOfFirst { it is GlobalNavViewModel }
}

internal fun List<Visitable<*>>.getFirstTopAdsProductPosition(): Int {
    return indexOfFirst { it is ProductItemViewModel && it.isTopAds }
}

internal fun List<Visitable<*>>.getFirstOrganicProductPosition(): Int {
    return indexOfFirst { it is ProductItemViewModel && !it.isTopAds }
}

internal fun List<Visitable<*>>.getEmptySearchProductViewModelPosition(): Int {
    return indexOfFirst { it is EmptySearchProductViewModel }
}

internal fun List<Visitable<*>>.getRecommendationTitleViewModelPosition(): Int {
    return indexOfFirst { it is RecommendationTitleViewModel }
}

internal fun List<Visitable<*>>.getRecommendationItemViewModelPosition(): Int {
    return indexOfFirst { it is RecommendationItemViewModel }
}

internal fun List<Visitable<*>>.getSuggestionViewModelPosition(): Int {
    return indexOfFirst { it is SuggestionViewModel }
}

internal fun List<Visitable<*>>.getBroadMatchViewModelPosition(): Int {
    return indexOfFirst { it is BroadMatchViewModel }
}