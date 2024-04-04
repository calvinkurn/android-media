package com.tokopedia.seller.menu.common.constant

/**
 * Created By @ilhamsuaib on 18/01/21
 */

object Constant {

    const val INVALID_NUMBER_OF_FOLLOWERS: Long = -1

    const val D_DAY_PERIOD_TYPE_PM_PRO = "charging_period_pm_pro"
    const val COMMUNICATION_PERIOD_PM_PRO = "communication_period_pm_pro"

    const val TRANSACTION_RM_SUCCESS = "tx_success"

    object ShopStatus {

        const val THRESHOLD_TRANSACTION = 110
        const val MAX_TRANSACTION = 100

        const val ROUNDED_RADIUS = 16f

    }

    object Kyc {
        const val ACTIVE = 0
        const val INACTIVE = 1
    }
}
