package com.tokopedia.home_recom.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by dhaba
 */
object HomeRecomMockValueHelper {
    private const val DISABLE_RECOM_USING_GQL_FED = "false"

    fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(
            InstrumentationRegistry.getInstrumentation().context
        )
        remoteConfig.setString(RemoteConfigKey.RECOM_USE_GQL_FED_QUERY, DISABLE_RECOM_USING_GQL_FED)
    }
}
