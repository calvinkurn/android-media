package com.tokopedia.minicart.common.config

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

class MiniCartRemoteConfig @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val remoteConfig = FirebaseRemoteConfigImpl(context)

    companion object {
        private const val ENABLE_NEW_MINI_CART = "android_enable_new_mini_cart"
    }

    fun isNewMiniCartEnabled(): Boolean {
        return remoteConfig.getBoolean(ENABLE_NEW_MINI_CART, true)
    }
}