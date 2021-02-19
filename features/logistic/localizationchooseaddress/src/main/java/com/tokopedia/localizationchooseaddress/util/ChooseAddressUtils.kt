package com.tokopedia.localizationchooseaddress.util

import android.content.ClipData
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import javax.inject.Inject

class ChooseAddressUtils {

    fun getLocalizingAddressData(context: Context): LocalCacheModel {
        TODO("Kasih logic if rollence aktif, if login user, etc")
        var chooseAddressPref = ChooseAddressSharePref(context)
        return chooseAddressPref?.getLocalCacheData()
    }

    /**
     * Host must check this onResume
     * if the result true
     * then host must getLocalizingAddressData to refresh the page content
     * if false
     * it mean data has same. no need action from host
     */
    fun isLocalizingAddressHasUpdated(context: Context, localizingAddressStateData: LocalCacheModel): Boolean {
        var chooseAddressPref = ChooseAddressSharePref(context)
        var latestChooseAddressData = chooseAddressPref?.getLocalCacheData()
        var validate = true
        if (latestChooseAddressData.address_id != localizingAddressStateData.address_id) validate = false
        if (latestChooseAddressData.city_id != localizingAddressStateData.city_id) validate = false
        if (latestChooseAddressData.district_id != localizingAddressStateData.district_id) validate = false
        if (latestChooseAddressData.lat != localizingAddressStateData.lat) validate = false
        if (latestChooseAddressData.long != localizingAddressStateData.long) validate = false
        if (latestChooseAddressData.label != localizingAddressStateData.label) validate = false
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