package com.tokopedia.sellerhomecommon.sse

import com.tokopedia.sellerhomecommon.sse.model.SSEModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

interface SellerHomeCommonSSE {

    fun connect(page: String, dataKeys: List<String>)

    fun close()

    fun listen(): Flow<SSEModel>
}
