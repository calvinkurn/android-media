package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.model.Configuration

/**
 * @author okasurya on 10/9/18.
 */
interface Iris {

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

        fun convert(json: String) : Configuration? {
            return ConfigurationMapper().parse(json)
        }
    }
}