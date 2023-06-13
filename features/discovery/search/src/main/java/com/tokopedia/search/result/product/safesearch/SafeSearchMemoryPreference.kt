package com.tokopedia.search.result.product.safesearch

import java.util.concurrent.atomic.AtomicBoolean

object SafeSearchMemoryPreference : MutableSafeSearchPreference {
    private var atomicShowAdult: AtomicBoolean = AtomicBoolean(false)
    override var isShowAdult: Boolean
        get() = atomicShowAdult.get()
        set(value) = atomicShowAdult.set(value)
}
