package com.tokopedia.privacycenter.consentwithdrawal.ui

import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentPurposeItemDataModel

object ConsentWithdrawalListener {
    interface Mandatory {
        fun onActivationButtonClicked(
            position: Int,
            isActive: Boolean,
            data: ConsentPurposeItemDataModel
        )

        fun onToggleClicked(
            position: Int,
            isActive: Boolean,
            data: ConsentPurposeItemDataModel
        )
    }
}
