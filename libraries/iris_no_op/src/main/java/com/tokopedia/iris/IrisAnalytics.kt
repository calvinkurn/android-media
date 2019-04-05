package com.tokopedia.iris

import com.tokopedia.iris.Iris
import com.tokopedia.iris.model.Configuration
import android.content.Context

/**
 * @author ricoharisin .
 */

class IrisAnalytics(context: Context) : Iris {
    override fun setService(config: Configuration) {
    }

    override fun resetService(config: Configuration) {
    }

    override fun saveEvent(map: Map<String, Any>) {
    }

    override fun sendEvent(map: Map<String, Any>) {
    }

    override fun setUserId(userId: String) {
    }

    override fun setDeviceId(deviceId: String) {
    }

}