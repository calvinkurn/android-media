package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.model.Configuration

interface Iris {

    fun setService(config: String, isEnabled: Boolean)

    fun setService(config: Configuration)

    fun resetService(config: Configuration)

    /**
     * save event to persistence storage,
     * the events will be sent to server periodically
     *
     */
    fun saveEvent(map: Map<String, Any>)

    /**
     * direct send event to server
     */
    fun sendEvent(map: Map<String, Any>)

    fun setUserId(userId: String)

    fun setDeviceId(deviceId: String)

    companion object {
        fun getInstance(context: Context) : Iris {
            return IrisAnalytics(context)
        }

        fun deleteInstance() {
        }
    }
}