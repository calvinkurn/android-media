package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.model.Configuration

/**
 * @author okasurya on 10/9/18.
 */
interface Iris {

    /**
     * set custom configuration by json
     * param:
     * config, ex: {"row_limit":25,"interval":1}
     * isEnabled, ex: true
     */
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

        private val lock = Any()

        @Volatile private var iris: Iris? = null

        fun getInstance(context: Context) : Iris {
            return iris?: synchronized(lock) {
                IrisAnalytics(context).also {
                    iris = it
                }
            }
        }

        fun deleteInstance() {
            synchronized(lock) {
                iris = null
            }
        }
    }
}