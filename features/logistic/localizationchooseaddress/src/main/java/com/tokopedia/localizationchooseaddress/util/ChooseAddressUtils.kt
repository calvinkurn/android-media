package com.tokopedia.localizationchooseaddress.util

import android.content.ClipData
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

object ChooseAddressUtils {

    fun getLocalizingAddressData(context: Context): LocalCacheModel {
        if(isRollOutUser()){
            if(isLoginUser(context)){
                if(hasLocalizingAddressOnCache()){
                    var chooseAddressPref = ChooseAddressSharePref(context)
                    return chooseAddressPref?.getLocalCacheData()
                }else{
                    return ChooseAddressConstant.emptyAddress
                }
            }else{
                return ChooseAddressConstant.defaultAddress
            }
        }else{
            return ChooseAddressConstant.emptyAddress
        }
    }

    private fun hasLocalizingAddressOnCache(): Boolean {
        TODO("Not yet implemented, check cachenya kosong atau ga")

    }

    private fun isLoginUser(context: Context): Boolean {
        var userSession: UserSessionInterface = UserSession(context)
        return userSession.isLoggedIn
    }

    fun isRollOutUser(): Boolean {
        TODO("Not yet implemented. atur logic terserah bisa langsung ke endpoint ui atau mau bikin local cache ")
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

    fun setLocalizingAddressData(addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String): LocalCacheModel {
        return LocalCacheModel(
                address_id = addressId,
                city_id = cityId,
                district_id = districtId,
                lat = lat,
                long = long,
                label = label
        )
    }
    /**
     * Host can use this for show or not coachmark
     * we not provide coackmark. just KEY for identifier.
     * after host page shown the coachmark, please trigger coachMarkLocalizingAddressAlreadyShown
     * coachmark must implemented by own host page
     */
    fun isLocalizingAddressNeedShowCoachMark(): Boolean {
        //cek sharedPref
        TODO("Not yet implemented, untuk HOST supaya tau harus munculin coachmak atau ga. bisa bikin key di sharepref")
        return true
    }

    fun coachMarkLocalizingAddressAlreadyShown(){
        //update sharedPref
        TODO("Not yet implemented, update KEY identifiernya ya")
    }


}