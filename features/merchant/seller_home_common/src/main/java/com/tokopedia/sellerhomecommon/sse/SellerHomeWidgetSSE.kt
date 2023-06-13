package com.tokopedia.sellerhomecommon.sse

import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by @ilhamsuaib on 10/10/22.
 */

interface SellerHomeWidgetSSE {

    fun connect(page: String, dataKeys: List<String>)

    fun closeSse()

    fun listen(): Flow<BaseDataUiModel?>

    fun isConnected(): Boolean
}
