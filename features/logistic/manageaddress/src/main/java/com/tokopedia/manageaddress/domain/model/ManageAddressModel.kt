package com.tokopedia.manageaddress.domain.model

sealed class ManageAddressModelSealed

data class ManageAddressModel(
        var liistAddress: List<PeopleAddress> = emptyList(),
        var token: Token? = null
)

data class PeopleAddress(
        var id: Int? = -1,
        var receiverName: String? = null,
        var addressName: String? = null,
        var receiverAddress: String? = null,
        var postalCode: String? = null,
        var provinceId: Int? = -1,
        var cityId: Int? = -1,
        var districtId: Int? = -1,
        var phoneNumber: String? = null,
        var countryName: String? = null,
        var provinceName: String? = null,
        var cityName: String? = null,
        var districtName: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var isPrimary: Boolean? = false,
        var status: Int? = null
)

data class Token(
        var ut: Int? = -1,
        var districtRecommendation: String? = null
)
