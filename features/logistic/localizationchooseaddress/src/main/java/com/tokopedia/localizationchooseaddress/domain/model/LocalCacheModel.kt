package com.tokopedia.localizationchooseaddress.domain.model

data class LocalCacheModel (
        var address_id: String = "",
        var city_id: String = "",
        var district_id: String = "",
        var lat: String = "",
        var long: String = "",
        var postal_code: String = "",
        var label: String = ""
)