package com.tokopedia.search.result.product.similarsearch

import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface SimilarSearchOnBoardingView {
    fun showSimilarSearchThreeDotsCoachmark(
        item: ProductItemDataView,
        adapterPosition: Int,
    )
}
