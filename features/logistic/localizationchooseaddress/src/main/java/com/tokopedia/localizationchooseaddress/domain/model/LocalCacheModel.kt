package com.tokopedia.localizationchooseaddress.domain.model

data class LocalCacheModel (
        val address_id: String = "",
        val city_id: String = "",
        val district_id: String = "",
        val lat: String = "",
        val long: String = "",
        val postal_code: String = "",
        val label: String = "",
        val shop_id: String = "",
        val warehouse_id: String = ""
)