package com.tokopedia.topads.sdk.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_ADDRESS_ID
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_CITY_ID
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_DISTRICT_ID
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_LAT
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_LONG
import com.tokopedia.topads.sdk.TopAdsConstants.TopAdsAddressConstant.USER_POSTCODE
import javax.inject.Inject

class TopAdsAddressHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getAddressData(): Map<String, String> {
        val addressData = ChooseAddressUtils.getLocalizingAddressData(context)

        val map = hashMapOf<String, String>()
        if (addressData.district_id.isNotEmpty()) map[USER_DISTRICT_ID] = addressData.district_id
        if (addressData.city_id.isNotEmpty()) map[USER_CITY_ID] = addressData.city_id
        if (addressData.postal_code.isNotEmpty()) map[USER_POSTCODE] = addressData.postal_code
        if (addressData.lat.isNotEmpty()) map[USER_LAT] = addressData.lat
        if (addressData.long.isNotEmpty()) map[USER_LONG] = addressData.long
        if (addressData.address_id.isNotEmpty()) map[USER_ADDRESS_ID] = addressData.address_id

        return map
    }
}
