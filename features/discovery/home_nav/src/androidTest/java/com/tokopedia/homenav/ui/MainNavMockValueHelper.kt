package com.tokopedia.homenav.ui

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object MainNavMockValueHelper {
    // buyer + seller saldo
    //response_success_mock_mainnav_saldo
    const val MOCK_VALUE_SALDO = "Rp38.000"

    //response_success_mock_mainnav_walletapp
    const val MOCK_VALUE_GOPAY = "Rp500.000"

    //response_success_mock_mainnav_walletapp
    const val MOCK_VALUE_GOPAY_COINS = "Rp500.000"

    //response_success_mock_mainnav_shopinfo
    const val MOCK_VALUE_SHOP_NAME = "Toko Deva Ntap"
    //neworder + readytoship
    const val MOCK_VALUE_SHOP_NOTIF = "1500"

    //response_success_mock_mainnav_ovo
    const val MOCK_VALUE_OVO = "Rp 100.000"
    const val MOCK_VALUE_OVO_POINTS = "100.000"

    //response_success_mock_mainnav_tokopoints_filtered
    const val MOCK_VALUE_TOKOPOINTS_AMOUNT = "100.000"
    const val MOCK_VALUE_TOKOPOINTS_EXTERNAL_AMOUNT = "Rp100.000"

    //response_success_mock_mainnav_profile
    const val MOCK_VALUE_USERNAME = "Devara Magician"

    fun setupAbTestRemoteConfig() {}
}