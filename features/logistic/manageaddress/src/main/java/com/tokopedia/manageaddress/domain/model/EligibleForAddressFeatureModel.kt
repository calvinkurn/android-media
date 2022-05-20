package com.tokopedia.manageaddress.domain.model

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

data class EligibleForAddressFeatureModel(
    val featureId: Int = 0,
    val error: String = "",
    val eligible: Boolean = false,
    val data: RecipientAddressModel? = null
)
