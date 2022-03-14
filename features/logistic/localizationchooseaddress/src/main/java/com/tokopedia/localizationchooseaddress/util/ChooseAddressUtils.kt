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
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.ui.preference.CoachMarkStateSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.DEFAULT_LCA_VERSION
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.LCA_VERSION
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber

object ChooseAddressUtils {

    private const val locationParamsFormat: String =
        "user_lat=%s&user_long=%s&user_addressId=%s&user_cityId=%s&user_districtId=%s&user_postCode=%s&warehouse_ids=%s"
    private const val locationRequestInterval = 10 * 1000L
    private const val locationRequestFastestInterval = 2 * 1000L

    fun getLocalizingAddressData(context: Context): LocalCacheModel {
        return if (hasLocalizingAddressOnCache(context)) {
            val chooseAddressPref = ChooseAddressSharePref(context)
            val localCache = chooseAddressPref.getLocalCacheData()
            LocalCacheModel(
                localCache?.address_id.checkIfNumber("address_id"),
                localCache?.city_id ?: "",
                localCache?.district_id.checkIfNumber("district_id"),
                localCache?.lat ?: "",
                localCache?.long ?: "",
                localCache?.postal_code ?: "",
                localCache?.label ?: "",
                localCache?.shop_id ?: "",
                localCache?.warehouse_id ?: "",
                localCache?.warehouses ?: listOf(),
                localCache?.service_type ?: "",
                localCache?.version ?: DEFAULT_LCA_VERSION
            )
        } else {
            if (isLoginUser(context)) {
                ChooseAddressConstant.emptyAddress
            } else {
                ChooseAddressConstant.defaultAddress
            }
        }

    }

    fun getLocalizingAddressDataDirectly(context: Context): LocalCacheModel? {
        return if (isLoginUser(context)) {
            ChooseAddressConstant.emptyAddress
        } else {
            ChooseAddressConstant.defaultAddress
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
            if (latestChooseAddressData.shop_id != localizingAddressStateData.shop_id) validate = true
            if (latestChooseAddressData.warehouse_id != localizingAddressStateData.warehouse_id) validate = true
            if (latestChooseAddressData.warehouses != localizingAddressStateData.warehouses) validate = true
            if (latestChooseAddressData.service_type != localizingAddressStateData.service_type) validate = true
            if (latestChooseAddressData.version != localizingAddressStateData.version) validate = true
        }
        return validate
    }

    fun setLocalizingAddressData(addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String,
                                 postalCode: String, shopId: String, warehouseId: String, warehouses: List<LocalWarehouseModel>, serviceType: String): LocalCacheModel {
        return LocalCacheModel(
                address_id = addressId,
                city_id = cityId,
                district_id = districtId,
                lat = lat,
                long = long,
                label = label,
                postal_code = postalCode,
                shop_id = shopId,
                warehouse_id = warehouseId,
                warehouses = warehouses,
                service_type = serviceType,
                version = LCA_VERSION
        )
    }

    fun updateLocalizingAddressDataFromOther(context: Context, addressId: String, cityId: String, districtId: String, lat: String, long: String, label: String,
                                             postalCode: String, shopId: String, warehouseId: String, warehouses: List<LocalWarehouseModel>, serviceType: String) {
        val chooseAddressPref = ChooseAddressSharePref(context)
        val localData = setLocalizingAddressData(addressId = addressId, cityId = cityId, districtId = districtId, lat = lat, long = long, label = label, postalCode = postalCode, shopId = shopId, warehouseId = warehouseId, warehouses = warehouses, serviceType = serviceType)
        chooseAddressPref.setLocalCache(localData)
    }

    fun updateLocalizingAddressDataFromOther(context: Context, localData: LocalCacheModel) {
        val chooseAddressPref = ChooseAddressSharePref(context)
        chooseAddressPref.setLocalCache(localData)
    }

    fun updateTokoNowData(context: Context, warehouseId: String, shopId: String, warehouses: List<LocalWarehouseModel>, serviceType: String) {
        val chooseAddressPref = ChooseAddressSharePref(context)
        val newData = getLocalizingAddressData(context).copy(warehouse_id = warehouseId, shop_id = shopId, warehouses = warehouses, service_type = serviceType, version = LCA_VERSION)
        chooseAddressPref.setLocalCache(newData)
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
            locationRequest.interval = locationRequestInterval
            locationRequest.fastestInterval = locationRequestFastestInterval
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
        return String.format(
            locationParamsFormat,
            lat,
            long,
            address_id,
            city_id,
            district_id,
            postal_code,
            warehouse_id
        )
    }

    fun setLabel(data: ChosenAddressModel) : String {
        return if (data.addressName.isEmpty() || data.receiverName.isEmpty()) {
            "${data.districtName}, ${data.cityName}"
        } else {
            "${data.addressName} ${data.receiverName}"
        }
    }

    internal fun String?.checkIfNumber(key: String): String {
        if (this == null || this.isEmpty()) return ""

        return try {
            this.toLong()
            this
        } catch (t: Throwable) {
            Timber.d(t)
            ChooseAddressLogger.logOnLocalizing(t,key, this)
            ""
        }
    }

}