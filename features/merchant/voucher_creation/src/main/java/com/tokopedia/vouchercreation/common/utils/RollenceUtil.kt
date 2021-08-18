package com.tokopedia.vouchercreation.common.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.BROADCAST_VOUCHER_AB_TEST_KEY

object RollenceUtil {

    private const val KEY_BROADCAST_VOUCHER = "broadcast_voucher"

    fun getBroadCastVoucherRollenceValue(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform
            .getString(BROADCAST_VOUCHER_AB_TEST_KEY, "")
            .equals(KEY_BROADCAST_VOUCHER, ignoreCase = false)
    }
}