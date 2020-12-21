package com.tokopedia.search.testcase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel

internal fun List<Visitable<*>>.getGlobalNavViewModelPosition(): Int {
    return indexOfFirst { it is GlobalNavViewModel }
}

internal fun List<Visitable<*>>.getGlobalNavViewModel(): GlobalNavViewModel {
    return this[indexOfFirst { it is GlobalNavViewModel }] as GlobalNavViewModel
}

private fun List<Visitable<*>>.getFirstTopAdsProductPosition(): Int {
    return indexOfFirst { it is ProductItemViewModel && it.isTopAds }
}

private fun List<Visitable<*>>.getFirstOrganicProductPosition(): Int {
    return indexOfFirst { it is ProductItemViewModel && !it.isTopAds }
}