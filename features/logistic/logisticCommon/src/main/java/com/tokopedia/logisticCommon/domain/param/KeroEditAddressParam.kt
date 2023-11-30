package com.tokopedia.logisticCommon.domain.param

import com.tokopedia.logisticCommon.data.constant.ManageAddressSource

data class KeroEditAddressParam(
    val addressId: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val source: ManageAddressSource
)
