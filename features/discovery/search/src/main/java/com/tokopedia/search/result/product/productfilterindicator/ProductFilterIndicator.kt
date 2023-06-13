package com.tokopedia.search.result.product.productfilterindicator

interface ProductFilterIndicator {
    val isAnyFilterActive: Boolean
    val isAnySortActive: Boolean
    val isAnyFilterOrSortActive: Boolean
}
