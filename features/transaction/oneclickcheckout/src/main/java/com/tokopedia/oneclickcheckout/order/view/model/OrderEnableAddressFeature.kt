package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData

data class OrderEnableAddressFeature(
    val eligibleForAddressFeatureData: KeroAddrIsEligibleForAddressFeatureData = KeroAddrIsEligibleForAddressFeatureData(),
    val token: Token? = null)