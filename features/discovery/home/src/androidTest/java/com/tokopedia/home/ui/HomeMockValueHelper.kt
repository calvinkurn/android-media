package com.tokopedia.home.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object HomeMockValueHelper {
    const val DEFAULT_COUNTER_NOTIF_VALUE = 10
    const val DEFAULT_COUNTER_INBOX_TALK_VALUE = 10
    const val DEFAULT_COUNTER_INBOX_REVIEW_VALUE = 10
    const val DEFAULT_COUNTER_INBOX_TICKET_VALUE = 10
    const val DEFAULT_COUNTER_CHAT_UNREAD_VALUE = 0

    const val MOCK_HEADER_COUNT = 1

    const val MOCK_ATF_COUNT = 5
    const val MOCK_ATF_ERROR_COUNT = 3
    const val MOCK_DYNAMIC_CHANNEL_COUNT = 38
    const val MOCK_DYNAMIC_CHANNEL_ERROR_COUNT = 2
    const val MOCK_RECOMMENDATION_TAB_COUNT = 1
    private const val MOCK_DISABLE_REMOVE_PAGINATION = "false"
    private const val MOCK_ENABLE_DYNAMIC_CHANNEL_QUERY_V2 = "true"
    private const val MOCK_DISABLE_DYNAMIC_CHANNEL_QUERY_V2 = "false"

    fun setupAbTestRemoteConfig(
        atf2Rollence: Boolean = false,
        useHeaderRevamp: Boolean = true
    ) {
        val atf2Variant = if (atf2Rollence) RollenceKey.HOME_COMPONENT_ATF_2 else ""
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            RollenceKey.HOME_COMPONENT_ATF,
            atf2Variant
        )
        val headerVariant = if (useHeaderRevamp) RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_VARIANT else ""
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            RollenceKey.HOME_COMPONENT_DYNAMIC_CHANNEL_HEADER_EXP,
            headerVariant
        )
    }

    fun setupRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfigImpl(
            InstrumentationRegistry.getInstrumentation().context
        )
        remoteConfig.setString(RemoteConfigKey.HOME_REMOVE_PAGINATION, MOCK_DISABLE_REMOVE_PAGINATION)
    }

    fun setupDynamicChannelQueryRemoteConfig(
        isUsingQueryV2: Boolean = true
    ) {
        val remoteConfig = FirebaseRemoteConfigImpl(
            InstrumentationRegistry.getInstrumentation().context
        )
        val remoteConfigValue = if (isUsingQueryV2) MOCK_ENABLE_DYNAMIC_CHANNEL_QUERY_V2 else MOCK_DISABLE_DYNAMIC_CHANNEL_QUERY_V2
        remoteConfig.setString(RemoteConfigKey.HOME_DC_USE_QUERY_V2, remoteConfigValue)
    }
}
