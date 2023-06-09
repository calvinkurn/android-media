package com.tokopedia.localizationchooseaddress.util

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

class ChooseAddressConstant {

    companion object {
        const val EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA"
        const val EXTRA_IS_FROM_ANA = "EXTRA_IS_FROM_ANA"

        const val ERROR_CODE_EMPTY_LAT_LONG_PARAM = 3
        const val ERROR_CODE_INVALID_LAT_LONG_PARAM = 4
        const val ERROR_CODE_FAILED_GET_DISTRICT_DATA = 5
        const val ERROR_CODE_EMPTY_DISTRICT_DATA = 6
        const val ERROR_CODE_EMPTY_STATE_CHOSEN_ADDRESS = 9
        const val DEFAULT_LCA_VERSION = "1"
        const val LCA_VERSION = "1.3"

        val defaultAddress = LocalCacheModel(
            address_id = "",
            city_id = "176",
            district_id = "2274",
            lat = "",
            long = "",
            postal_code = "",
            label = "Jakarta Pusat",
            shop_id = "",
            warehouse_id = "",
            warehouses = listOf(),
            service_type = "",
            version = LCA_VERSION,
            tokonow_last_update = ""
        )

        val emptyAddress = LocalCacheModel(
            address_id = "",
            city_id = "",
            district_id = "",
            lat = "",
            long = "",
            postal_code = "",
            label = "Pilih Alamat Pengirimanmu",
            shop_id = "",
            warehouse_id = "",
            service_type = "",
            warehouses = listOf(),
            version = LCA_VERSION,
            tokonow_last_update = ""
        )
    }
}
