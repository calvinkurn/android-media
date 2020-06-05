package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.model.Configuration

/**
 * @author okasurya on 10/9/18.
 */
interface Iris {

    /**
     * Simplify initialize iris
     */
    fun initialize()

    /**
     * set custom configuration by json
     * param:
     * config, ex: {"row_limit":25,"interval":1}
     * isEnabled, ex: true
     */
    fun setService(config: String, isEnabled: Boolean)

    fun setService(config: Configuration)

    /**
     * save event to persistence storage,
     * the events will be sent to server periodically
     *
     */
    fun saveEvent(map: Map<String, Any>)

    /**
     * direct send event to server
     */
    @Deprecated(message = "function should not be called directly", replaceWith = ReplaceWith(expression = "saveEvent(map)"))
    fun sendEvent(map: Map<String, Any>)

    fun setAlarm(isTurnOn: Boolean, force: Boolean)

}