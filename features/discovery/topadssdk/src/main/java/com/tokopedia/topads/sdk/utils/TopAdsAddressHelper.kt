package com.tokopedia.topads.sdk.utils

import android.content.Context
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

class TopAdsAddressHelper @Inject constructor(private val context: Context) {

    fun getAddressData(): Map<String, String> {
        val addressData = ChooseAddressUtils.getLocalizingAddressData(context)

        return mapOf(
            "user_districtId" to addressData.district_id,
            "user_cityId" to addressData.city_id,
            "user_postCode" to addressData.postal_code,
            "user_lat" to addressData.lat,
            "user_long" to addressData.long,
            "user_addressId" to addressData.address_id
        )
    }
}
