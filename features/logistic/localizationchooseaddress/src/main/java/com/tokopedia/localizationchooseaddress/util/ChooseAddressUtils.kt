package com.tokopedia.localizationchooseaddress.util

import android.content.Context
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

object ChooseAddressUtils {

    fun getLocalizingAddressData(context: Context): LocalCacheModel? {
        if(isRollOutUser()){
            if(isLoginUser(context)){
                if(hasLocalizingAddressOnCache(context)){
                    var chooseAddressPref = ChooseAddressSharePref(context)
                    return chooseAddressPref.getLocalCacheData()
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

    private fun hasLocalizingAddressOnCache(context: Context): Boolean {
        var chooseAddressPref = ChooseAddressSharePref(context)
        return !chooseAddressPref.checkLocalCache().isNullOrEmpty()

    }

    fun isLoginUser(context: Context): Boolean {
        var userSession: UserSessionInterface = UserSession(context)
        return userSession.isLoggedIn
    }

    fun isRollOutUser(): Boolean {
        return true
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
        var latestChooseAddressData = chooseAddressPref.getLocalCacheData()
        var validate = true
        if (latestChooseAddressData?.address_id != localizingAddressStateData.address_id) validate = false
        if (latestChooseAddressData?.city_id != localizingAddressStateData.city_id) validate = false
        if (latestChooseAddressData?.district_id != localizingAddressStateData.district_id) validate = false
        if (latestChooseAddressData?.lat != localizingAddressStateData.lat) validate = false
        if (latestChooseAddressData?.long != localizingAddressStateData.long) validate = false
        if (latestChooseAddressData?.label != localizingAddressStateData.label) validate = false
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
    fun isLocalizingAddressNeedShowCoachMark(context: Context): Boolean? {
        var chooseAddressPref = ChooseAddressSharePref(context)
        return chooseAddressPref.getCoachMarkState()
    }

    fun coachMarkLocalizingAddressAlreadyShown(context: Context) {
        var chooseAddressPref = ChooseAddressSharePref(context)
        chooseAddressPref.setCoachMarkState(true)
    }

}