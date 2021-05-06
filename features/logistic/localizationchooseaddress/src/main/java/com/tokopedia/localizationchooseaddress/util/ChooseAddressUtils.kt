package com.tokopedia.localizationchooseaddress.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.View
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.ui.preference.CoachMarkStateSharePref
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

object ChooseAddressUtils {

    fun getLocalizingAddressData(context: Context): LocalCacheModel? {
        return if (isRollOutUser(context)) {
            if (hasLocalizingAddressOnCache(context)) {
                val chooseAddressPref = ChooseAddressSharePref(context)
                chooseAddressPref.getLocalCacheData()
            } else {
                if (isLoginUser(context)) {
                    ChooseAddressConstant.emptyAddress
                } else {
                    ChooseAddressConstant.defaultAddress
                }
            }
        } else {
            ChooseAddressConstant.emptyAddress
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
     * Rollence key
     */
    fun isRollOutUser(context: Context?): Boolean {
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY, "")
        return rollenceValue == ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY
    }

    /**
     * Host must check this onResume
     * if the result true
     * then host must getLocalizingAddressData to refresh the page content
     * if false
     * it mean data has same. no need action from host
     */
    fun isLocalizingAddressHasUpdated(context: Context, localizingAddressStateData: LocalCacheModel): Boolean {
        val latestChooseAddressData = getLocalizingAddressData(context)
        var validate = false
        if (latestChooseAddressData != null) {
            if (latestChooseAddressData.address_id != localizingAddressStateData.address_id) validate = true
            if (latestChooseAddressData.city_id != localizingAddressStateData.city_id) validate = true
            if (latestChooseAddressData.district_id != localizingAddressStateData.district_id) validate = true
            if (latestChooseAddressData.lat != localizingAddressStateData.lat) validate = true
            if (latestChooseAddressData.long != localizingAddressStateData.long) validate = true
            if (latestChooseAddressData.label != localizingAddressStateData.label) validate = true
            if (latestChooseAddressData.postal_code != localizingAddressStateData.postal_code) validate = true
        }
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

    fun updateLocalizingAddressDataFromOther(context: Context, addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String, postalCode: String) {
        if (isRollOutUser(context)) {
            val chooseAddressPref = ChooseAddressSharePref(context)
            val localData = setLocalizingAddressData(addressId, cityId, districtId, lat, long, label, postalCode)
            chooseAddressPref.setLocalCache(localData)
        }
    }

    /**
     * Host can use this for show or not coachmark
     * we not provide coackmark. just KEY for identifier.
     * after host page shown the coachmark, please trigger coachMarkLocalizingAddressAlreadyShown
     * coachmark must implemented by own host page
     */
    fun isLocalizingAddressNeedShowCoachMark(context: Context): Boolean? {
        val coachMarkStatePref = CoachMarkStateSharePref(context)
        return coachMarkStatePref.getCoachMarkState()
    }

    fun coachMarkLocalizingAddressAlreadyShown(context: Context) {
        val coachMarkStatePref = CoachMarkStateSharePref(context)
        coachMarkStatePref.setCoachMarkState(false)
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

    @JvmStatic
    fun isGpsEnabled(context: Context?): Boolean {
        var isGpsOn = false
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mSettingsClient = LocationServices.getSettingsClient(it)

            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 10 * 1000
            locationRequest.fastestInterval = 2 * 1000
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val mLocationSettingsRequest = builder.build()
            builder.setAlwaysShow(true)

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGpsOn = true
            } else {
                mSettingsClient
                        .checkLocationSettings(mLocationSettingsRequest)
                        .addOnSuccessListener(context as Activity) {
                            isGpsOn = true
                        }
            }

            isGpsOn = isLocationEnabled(it) && isGpsOn
        }
        return isGpsOn
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
            mode != Settings.Secure.LOCATION_MODE_OFF

        }
    }

    fun LocalCacheModel.convertToLocationParams(): String {
        return "user_lat=" + lat +
                "&user_long=" + long +
                "&user_addressId=" + address_id +
                "&user_cityId=" + city_id +
                "&user_districtId=" + district_id +
                "&user_postCode=" + postal_code
    }

    fun setLabel(data: ChosenAddressModel) : String {
        return if (data.addressName.isEmpty() || data.receiverName.isEmpty()) {
            "${data.districtName}, ${data.cityName}"
        } else {
            "${data.addressName} ${data.receiverName}"
        }
    }
}