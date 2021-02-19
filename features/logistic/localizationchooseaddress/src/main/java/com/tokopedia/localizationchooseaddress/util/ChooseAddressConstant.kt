package com.tokopedia.localizationchooseaddress.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

class ChooseAddressConstant {

    companion object {
        val defaultAddress = LocalCacheModel(
                address_id = "",
                city_id = "176",
                district_id = "2274",
                lat = "",
                long = "",
                label = "Jakarta Pusat"
        )

        val emptyAddress = LocalCacheModel(
                address_id = "",
                city_id = "",
                district_id = "",
                lat = "",
                long = "",
                label = "Pilih Alamat Pengirimanmu"
        )
    }
}