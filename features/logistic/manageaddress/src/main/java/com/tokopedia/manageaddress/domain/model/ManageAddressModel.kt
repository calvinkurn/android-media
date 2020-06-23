package com.tokopedia.manageaddress.domain.model

import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.logisticdata.data.entity.address.Token

data class ManageAddressModel(
        var listAddress: List<AddressModel> = emptyList(),
        var token: Token? = null
)
