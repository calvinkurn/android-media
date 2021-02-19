package com.tokopedia.localizationchooseaddress.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import javax.inject.Inject

class ChooseAddressUtils {

    fun getLatestChooseAddress(context: Context): LocalCacheModel {
        var chooseAddressPref = ChooseAddressSharePref(context)
        return chooseAddressPref?.getLocalCacheData()
    }

    fun compareAddressData(context: Context, data: LocalCacheModel): Boolean {
        var chooseAddressPref = ChooseAddressSharePref(context)
        var latestChooseAddressData = chooseAddressPref?.getLocalCacheData()
        var validate = true
        if (latestChooseAddressData.address_id != data.address_id) validate = false
        if (latestChooseAddressData.city_id != data.city_id) validate = false
        if (latestChooseAddressData.district_id != data.district_id) validate = false
        if (latestChooseAddressData.lat != data.lat) validate = false
        if (latestChooseAddressData.long != data.long) validate = false
        if (latestChooseAddressData.label != data.label) validate = false
        return validate
    }

    fun setDataToLocalCache(addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String): LocalCacheModel {
        return LocalCacheModel(
                address_id = addressId,
                city_id = cityId,
                district_id = districtId,
                lat = lat,
                long = long,
                label = label
        )
    }


}