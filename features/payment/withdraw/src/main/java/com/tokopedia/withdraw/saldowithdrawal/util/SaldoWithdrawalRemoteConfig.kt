package com.tokopedia.withdraw.saldowithdrawal.util

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class SaldoWithdrawalRemoteConfig @Inject constructor(
        private val remoteConfig: RemoteConfig) {

    fun isRekeningPremiumLogoVisibleToAll(): Boolean {
        return remoteConfig.getBoolean(REKENING_PREMIUM_LOGO_AVAILABLE_TO_ALL, true)
    }

    companion object {
        private const val REKENING_PREMIUM_LOGO_AVAILABLE_TO_ALL = "app_rekening_premium_logo_visible_to_all"
    }

}