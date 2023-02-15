package com.tokopedia.talk.feature.sellersettings.settings.presentation.viewmodel

import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class TalkSettingsViewModelTest : TalkSettingsViewModelTestFixture() {
    @Test
    fun `getSmartReplyDecommissionConfig should success map config from remote config`() {
        val expected = SmartReplyTalkDecommissionConfig.TalkSettingPage(
            showSmartReplyEntryPoint = false
        )
        val partialRemoteConfigJson = """
            {
                  "talk_setting_page": {
                        "show_smart_reply_entrypoint": false
                  }
            }
        """.trimIndent()

        every {
            firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
        } returns partialRemoteConfigJson

        Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
    }

    @Test
    fun `getSmartReplyDecommissionConfig should set smartReplyDecommissionConfig default config when JSON mapping is error`() {
        val expected = SmartReplyTalkDecommissionConfig.TalkSettingPage(
            showSmartReplyEntryPoint = true
        )
        val partialRemoteConfigJson = "this is invalid json"

        every {
            firebaseRemoteConfig.getString("android_seller_app_smart_reply_talk_decommission_config")
        } returns partialRemoteConfigJson

        Assert.assertEquals(expected, viewModel.smartReplyDecommissionConfig.value)
    }
}
