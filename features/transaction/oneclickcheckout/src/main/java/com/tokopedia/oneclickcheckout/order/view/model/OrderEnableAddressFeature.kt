package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeature

data class OrderEnableAddressFeature(
        val eligibleForAddressFeature: KeroAddrIsEligibleForAddressFeature = KeroAddrIsEligibleForAddressFeature(),
        val token: Token? = null)