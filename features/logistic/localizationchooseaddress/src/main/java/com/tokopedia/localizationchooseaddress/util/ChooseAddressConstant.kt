package com.tokopedia.localizationchooseaddress.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

class ChooseAddressConstant {

    companion object {
        const val CHOOSE_ADDRESS_ROLLENCE_KEY = "hyperlocal_android"
        const val EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA"
        const val EXTRA_IS_FROM_ANA = "EXTRA_IS_FROM_ANA"

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