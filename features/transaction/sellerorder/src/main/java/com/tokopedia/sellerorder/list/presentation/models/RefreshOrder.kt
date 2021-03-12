package com.tokopedia.sellerorder.list.presentation.models

import kotlinx.coroutines.Job

data class RefreshOrder(
        val orderId: String,
        val invoice: String,
        var job: Job
)