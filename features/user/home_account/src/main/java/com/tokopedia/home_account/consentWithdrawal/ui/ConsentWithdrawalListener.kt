package com.tokopedia.home_account.consentWithdrawal.ui

import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeItemDataModel

object ConsentWithdrawalListener {
    interface Mandatory {
        fun onActivationButtonClicked(
            position: Int,
            isActive: Boolean,
            data: ConsentPurposeItemDataModel
        )
    }

    interface Optional {
        fun onToggleClicked(
            position: Int,
            isActive: Boolean,
            data: ConsentPurposeItemDataModel
        )
    }
}
