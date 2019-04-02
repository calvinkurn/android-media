package com.tokopedia.locationmanager

/**
 * @author by nisie on 29/01/19.
 */
data class DeviceLocation(val latitude: Double = 0.0,
                          val longitude: Double = 0.0,
                          val lastTimeRetrieved: Long = 0) {
}