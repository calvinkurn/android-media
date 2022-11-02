package com.tokopedia.search.result.product.safesearch

interface SafeSearchView {
    fun registerSameSessionListener(safeSearchPreference: MutableSafeSearchPreference)
    fun release()
}
