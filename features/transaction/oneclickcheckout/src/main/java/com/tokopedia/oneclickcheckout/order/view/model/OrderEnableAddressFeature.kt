package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse

data class OrderEnableAddressFeature(
    val eligibleForAddressFeatureResponse: KeroAddrIsEligibleForAddressFeatureResponse = KeroAddrIsEligibleForAddressFeatureResponse(),
    val token: Token? = null)