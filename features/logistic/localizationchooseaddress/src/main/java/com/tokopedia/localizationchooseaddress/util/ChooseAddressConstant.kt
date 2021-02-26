package com.tokopedia.localizationchooseaddress.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

class ChooseAddressConstant {

    companion object {
        const val CHOOSE_ADDRESS_ROLLENCE_KEY = "hyperlocal_android"
        const val INTENT_ADDRESS_SELECTED = "selected_address"

        val defaultAddress = LocalCacheModel(
                address_id = "",
                city_id = "176",
                district_id = "2274",
                lat = "",
                long = "",
                postal_code = "",
                label = "Jakarta Pusat"
        )

        val emptyAddress = LocalCacheModel(
                address_id = "",
                city_id = "",
                district_id = "",
                lat = "",
                long = "",
                postal_code = "",
                label = "Pilih Alamat Pengirimanmu"
        )
    }
}