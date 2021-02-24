package com.tokopedia.localizationchooseaddress.util

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

object ChooseAddressUtils {

    fun getLocalizingAddressData(context: Context): LocalCacheModel? {
        if(isRollOutUser(context)){
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
        val chooseAddressPref = ChooseAddressSharePref(context)
        return !chooseAddressPref.checkLocalCache().isNullOrEmpty()

    }

    fun isLoginUser(context: Context): Boolean {
        val userSession: UserSessionInterface = UserSession(context)
        return userSession.isLoggedIn
    }

    /**
     * temporary use return true
     */
    fun isRollOutUser(context: Context): Boolean {
        /*val chooseAddressPref = ChooseAddressSharePref(context)
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY, "")
        return if (rollenceValue == ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY) {
            chooseAddressPref.setRollenceValue(true)
            true
        } else {
            chooseAddressPref.setRollenceValue(false)
            false
        }*/

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
        var validate = false
        if (latestChooseAddressData?.address_id != localizingAddressStateData.address_id) validate = true
        if (latestChooseAddressData?.city_id != localizingAddressStateData.city_id) validate = true
        if (latestChooseAddressData?.district_id != localizingAddressStateData.district_id) validate = true
        if (latestChooseAddressData?.lat != localizingAddressStateData.lat) validate = true
        if (latestChooseAddressData?.long != localizingAddressStateData.long) validate = true
        if (latestChooseAddressData?.label != localizingAddressStateData.label) validate = true
        return validate
    }

    fun setLocalizingAddressData(addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String, postalCode: String): LocalCacheModel {
        return LocalCacheModel(
                address_id = addressId,
                city_id = cityId,
                district_id = districtId,
                lat = lat,
                long = long,
                label = label,
                postal_code = postalCode
        )
    }

    fun updateLocalizingAddressDataFromOther(context: Context, addressId: String, cityId: String, districtId: String, lat: String, long: String, addressName: String, postalCode: String) {
        val chooseAddressPref = ChooseAddressSharePref(context)
        val localData = setLocalizingAddressData(addressId, cityId, districtId, lat, long, addressName, postalCode)
        chooseAddressPref.setLocalCache(localData)
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

    fun coachMarkItem(context: Context, view: View) : CoachMarkItem {
        return CoachMarkItem(
                view,
                context.getString(R.string.coachmark_title),
                context.getString(R.string.coachmark_desc)
        )
    }

    fun coachMark2Item(context: Context, view: View) : CoachMark2Item {
        return CoachMark2Item(
                view,
                context.getString(R.string.coachmark_title),
                context.getString(R.string.coachmark_desc)
        )
    }

}