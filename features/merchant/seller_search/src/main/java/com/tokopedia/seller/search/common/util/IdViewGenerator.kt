package com.tokopedia.seller.search.common.util

import java.util.concurrent.atomic.AtomicInteger

object IdViewGenerator {
    private val counter = AtomicInteger(1)

    fun generateUniqueId(prefix: String): Int {
        return prefix.hashCode() + counter.getAndIncrement()
    }
}
