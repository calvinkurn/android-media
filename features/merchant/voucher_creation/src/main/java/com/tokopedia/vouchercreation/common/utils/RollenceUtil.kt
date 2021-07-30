package com.tokopedia.vouchercreation.common.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {

    private const val KEY_BROADCAST_VOUCHER = "broadcast_voucher"

    fun getBroadCastVoucherRollenceValue(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform
            .getBoolean(KEY_BROADCAST_VOUCHER, false)
    }
}