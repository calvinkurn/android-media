package com.tokopedia.moengage_wrapper

import android.content.Context
import com.tokopedia.moengage_wrapper.constants.Constants

class MoengageValidator {

    fun checkIfMoengageEnabled(context: Context): Boolean {
        val remoteConfigUtil = RemoteConfigUtil(context)
        val moengageEventsEnabled = remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_EVENTS, false)
        val moengageUserAttributeEnabled = remoteConfigUtil.getBooleanRemoteConfig(Constants.RemoteConfigConstants.APP_MOENGAGE_USER_ATTRIBUTE, false)
        return moengageEventsEnabled || moengageUserAttributeEnabled
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