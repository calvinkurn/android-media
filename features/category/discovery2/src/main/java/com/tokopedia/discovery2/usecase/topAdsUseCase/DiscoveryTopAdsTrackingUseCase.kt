package com.tokopedia.discovery2.usecase.topAdsUseCase

import com.tokopedia.topads.sdk.utils.ImpresionTask
import javax.inject.Inject

class DiscoveryTopAdsTrackingUseCase @Inject constructor() {

    fun sendTopAdsTracking(className: String?, url: String) {
        if (!className.isNullOrEmpty() && url.isNotEmpty()) {
            ImpresionTask(className).execute(url)
        }
    }
}