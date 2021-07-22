package com.tokopedia.home.ui

import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object HomeMockValueHelper {
    const val DEFAULT_COUNTER_NOTIF_VALUE = "10"

    const val MOCK_HEADER_COUNT = 1
    const val MOCK_ATF_COUNT = 5
    const val MOCK_ATF_ERROR_POSITION_COUNT = 1
    const val MOCK_ATF_ERROR_COUNT = 3
    const val MOCK_DYNAMIC_CHANNEL_COUNT = 19
    const val MOCK_DYNAMIC_CHANNEL_ERROR_COUNT = 2
    const val MOCK_RECOMMENDATION_TAB_COUNT = 1

    fun setupAbTestRemoteConfig(
        inboxRollence: Boolean = true,
        navigationRollence: Boolean = true,
        chooseAddressRollence: Boolean = true,
        balanceWidgetRollence: Boolean = true,
        homeRollence: Boolean = true,
        walletAppRollence: Boolean = true,
        paymentAbcRollence: Boolean = true
        ) {
        if (inboxRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.KEY_AB_INBOX_REVAMP,
                RollenceKey.VARIANT_NEW_INBOX
            )
        }
        if (navigationRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.NAVIGATION_EXP_TOP_NAV,
                RollenceKey.NAVIGATION_VARIANT_REVAMP
            )
        }
        if (chooseAddressRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY,
                ChooseAddressConstant.CHOOSE_ADDRESS_ROLLENCE_KEY
            )
        }
        if (balanceWidgetRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.BALANCE_EXP,
                RollenceKey.BALANCE_VARIANT_NEW
            )
        }
        if (homeRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.HOME_EXP,
                RollenceKey.HOME_VARIANT_REVAMP
            )
        }
        if (walletAppRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.HOME_WALLETAPP,
                RollenceKey.HOME_WALLETAPP
            )
        }
        if (paymentAbcRollence) {
            RemoteConfigInstance.getInstance().abTestPlatform.setString(
                RollenceKey.HOME_PAYMENT_ABC,
                RollenceKey.HOME_PAYMENT_ABC
            )
        }
    }
}