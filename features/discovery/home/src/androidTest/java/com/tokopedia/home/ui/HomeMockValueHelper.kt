package com.tokopedia.home.ui

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object HomeMockValueHelper {
    const val DEFAULT_COUNTER_NOTIF_VALUE = "10"

    const val MOCK_HEADER_COUNT = 1
    const val MOCK_HEADER_COUNT_NON_LOGIN = 0

    const val MOCK_ATF_COUNT = 5
    const val MOCK_ATF_ERROR_POSITION_COUNT = 1
    const val MOCK_ATF_ERROR_COUNT = 3
    const val MOCK_DYNAMIC_CHANNEL_COUNT = 23
    const val MOCK_DYNAMIC_CHANNEL_ERROR_COUNT = 2
    const val MOCK_RECOMMENDATION_TAB_COUNT = 1

    fun setupAbTestRemoteConfig(
        navigationRollence: Boolean = true,
        balanceWidgetRollence: Boolean = true,
        homeRollence: Boolean = true,
        walletAppRollence: Boolean = true,
        paymentAbcRollence: Boolean = true,
        navigationNewRollence: Boolean = true
        ) {
        if (balanceWidgetRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.BALANCE_EXP,
                RollenceKey.BALANCE_VARIANT_NEW
            )
        }
    }
}