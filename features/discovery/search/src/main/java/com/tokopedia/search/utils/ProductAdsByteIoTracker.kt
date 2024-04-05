package com.tokopedia.search.utils

import android.content.Context
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.search.result.presentation.model.ProductItemDataView

internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: ProductItemDataView?, refer: String) {
    element?.let {
        if (it.isAds) {
            AppLogTopAds.sendEventRealtimeClick(
                context,
                PageName.SEARCH_RESULT,
                it.asAdsLogRealtimeClickModel(refer)
            )
        }
    }
}
