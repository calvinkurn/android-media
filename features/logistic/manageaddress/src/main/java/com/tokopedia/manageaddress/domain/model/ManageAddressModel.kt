package com.tokopedia.manageaddress.domain.model

data class ManageAddressModel(
        var id: String? = null,
        var provinceName: String? = null,
        var provinceCode: String? = null,
        var postalCode: String? = null,
        var status: Int? = null
)
