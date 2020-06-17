package com.tokopedia.manageaddress.domain.model

import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.logisticdata.data.entity.address.Token

sealed class ManageAddressModelSealed

data class ManageAddressModel(
        var listAddress: List<AddressModel> = emptyList(),
        var token: Token? = null
)

data class Token(
        var ut: Int? = -1,
        var districtRecommendation: String? = null
)
