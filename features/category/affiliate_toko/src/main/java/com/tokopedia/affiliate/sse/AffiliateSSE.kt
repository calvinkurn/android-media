package com.tokopedia.affiliate.sse

import com.tokopedia.affiliate.sse.model.AffiliateSSEAction
import kotlinx.coroutines.flow.Flow

interface AffiliateSSE {

    fun connect(pageSource: String, authToken: String)

    fun close()

    fun listen(): Flow<AffiliateSSEAction>
}
