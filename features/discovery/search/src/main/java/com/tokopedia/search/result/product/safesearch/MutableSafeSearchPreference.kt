package com.tokopedia.search.result.product.safesearch

interface MutableSafeSearchPreference : SafeSearchPreference {
    override var isShowAdult: Boolean
}
