package com.tokopedia.tokofood.common.util

import android.content.Context
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils

object TokofoodAddressExt {

    fun Context.updateLocalChosenAddressPinpoint(latitude: String, longitude: String) {
        val currentAddressData = ChooseAddressUtils.getLocalizingAddressData(this)
        currentAddressData.let { chooseAddressData ->
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = this,
                addressId = chooseAddressData.address_id,
                cityId = chooseAddressData.city_id,
                districtId = chooseAddressData.district_id,
                lat = latitude,
                long = longitude,
                label = chooseAddressData.label,
                postalCode = chooseAddressData.postal_code,
                warehouseId = chooseAddressData.warehouse_id,
                shopId = chooseAddressData.shop_id,
                warehouses = chooseAddressData.warehouses,
                serviceType = chooseAddressData.service_type,
                lastUpdate = chooseAddressData.tokonow_last_update
            )
        }
    }
}
