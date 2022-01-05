package com.tokopedia.tokopedianow.common.util

import com.tokopedia.tokopedianow.common.domain.model.ServiceType

object TokoNowServiceTypeUtil {
    fun getDeliveryDurationCopy(serviceType: ServiceType, fifteenMinCopy: String, twoHrCopy: String) = if (serviceType == ServiceType.NOW_15M) fifteenMinCopy else twoHrCopy

    fun getDeliveryDurationCopyRes(serviceType: ServiceType, fifteenMinCopyRes: Int, twoHrCopyRes: Int) = if (serviceType == ServiceType.NOW_15M) fifteenMinCopyRes else twoHrCopyRes
}