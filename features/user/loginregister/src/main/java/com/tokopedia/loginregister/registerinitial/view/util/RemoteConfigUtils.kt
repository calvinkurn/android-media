package com.tokopedia.loginregister.registerinitial.view.util

import android.content.Context
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

private fun firebaseRemoteConfig(context: Context) = FirebaseRemoteConfigImpl(context)

fun isRedefineRegisterEmailActivated(context: Context): Boolean =
    firebaseRemoteConfig(context).getBoolean(
        RegisterConstants.RemoteConfigKey.REMOTE_CONFIG_KEY_REGISTER_ONLY_WITH_PHONE_NUMBER,
        false
    )
