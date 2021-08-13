package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class SearchProductTopAdsImageDataView(
        val topAdsImageViewModel: TopAdsImageViewModel
): Visitable<ProductListTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}