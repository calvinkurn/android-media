package com.tokopedia.moengage_wrapper

import android.content.Context
import com.tokopedia.moengage_wrapper.constants.Constants

class MoengageValidator {

    fun checkIfMoengageEnabled(context: Context, yes: () -> Unit = {}, no: () -> Unit = {}) : Boolean {
        val remoteConfigUtil = RemoteConfigUtil(context)
        val moengageEventsEnabled = remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_EVENTS, false)
        val moengageUserAttributeEnabled = remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_USER_ATTRIBUTE, false)
        return if ( moengageEventsEnabled || moengageUserAttributeEnabled) {
            yes()
            true
        }
        else {
            no()
            false
        }
    }

    fun checkIfMoengageEventsEnabled(context: Context): Boolean {
        val remoteConfigUtil = RemoteConfigUtil(context)
        return remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_EVENTS, false)
    }

    fun checkIfMoengageUserAttributesEnabled(context: Context): Boolean {
        val remoteConfigUtil = RemoteConfigUtil(context)
        return remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_USER_ATTRIBUTE, false)
    }
}