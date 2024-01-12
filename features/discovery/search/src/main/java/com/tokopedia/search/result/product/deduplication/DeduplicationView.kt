package com.tokopedia.search.result.product.deduplication

interface DeduplicationView {

    fun trackRemoved(
        componentID: String,
        applink: String,
        externalReference: String,
    )
}
