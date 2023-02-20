package com.tokopedia.home_recom.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by dhaba
 */
object HomeRecomMockValueHelper {
    private const val ENABLE_RECOM_USING_GQL_FED = "true"

    fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(
            InstrumentationRegistry.getInstrumentation().context
        )
        remoteConfig.setString(RemoteConfigKey.RECOM_USE_GQL_FED_QUERY, ENABLE_RECOM_USING_GQL_FED)
    }
}
