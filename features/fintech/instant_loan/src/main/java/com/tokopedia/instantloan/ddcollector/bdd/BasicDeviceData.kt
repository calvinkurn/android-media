package com.tokopedia.instantloan.ddcollector.bdd


import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build

import com.tokopedia.instantloan.ddcollector.BaseCollector
import com.tokopedia.instantloan.ddcollector.util.AppInfo
import com.tokopedia.instantloan.ddcollector.util.DeviceInfo

import java.util.ArrayList
import java.util.HashMap

class BasicDeviceData(private val mContext: Context, private val mLocationManager: LocationManager) : BaseCollector() {

    override fun getType(): String {
        return DD_BASIC_DEVICE_DATA
    }

    @SuppressLint("MissingPermission")
    override fun getData(): Any {
        val phoneInfoList = ArrayList<HashMap<String, String?>>()

        try {
            val basicInfoMap = HashMap<String, String?>()
            basicInfoMap[DEVICE_ID] = DeviceInfo.getDeviceId(mContext)
            basicInfoMap[BRAND] = Build.BRAND
            basicInfoMap[GOOGLE_GAID] = "Stub"

            try {
                basicInfoMap[IMEI] = DeviceInfo.getImei(mContext)
            } catch (e: Exception) {
                basicInfoMap[IMEI] = "N/A"
            }

            basicInfoMap[IMEI] = AppInfo.getDefaultAcceptLanguage(mContext)

            try {
                basicInfoMap[LATITUDE] = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude.toString()
            } catch (npe: NullPointerException) {
                basicInfoMap[LATITUDE] = "0.0"
            }

            try {
                basicInfoMap[LONGITUDE] = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude.toString()
            } catch (npe: NullPointerException) {
                basicInfoMap[LONGITUDE] = "0.0"
            }

            basicInfoMap[MODEL] = DeviceInfo.getDeviceModelNumber(mContext)
            basicInfoMap[DEVICE_SDK_VERSION] = Build.VERSION.SDK_INT.toString()
            basicInfoMap[DEVICE_SYSTEM_VERSION] = Build.VERSION.RELEASE.toString()
            basicInfoMap[SYSTEM_LANGUAGE] = DeviceInfo.systemLanguage
            phoneInfoList.add(basicInfoMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return phoneInfoList
    }

    companion object {

        val DD_BASIC_DEVICE_DATA = "bdd"
        val BRAND = "brand"
        val DEVICE_ID = "device_id"
        val DEVICE_SDK_VERSION = "device_sdk_version"
        val DEVICE_SYSTEM_VERSION = "device_system_version"
        val GOOGLE_GAID = "google_gaid"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
        val MODEL = "model"
        val SYSTEM_LANGUAGE = "system_language"
        val IMEI = "imei"
    }
}
