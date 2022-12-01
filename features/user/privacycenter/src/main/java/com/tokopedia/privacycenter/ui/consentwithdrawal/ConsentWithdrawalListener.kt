package com.tokopedia.privacycenter.ui.consentwithdrawal

import com.tokopedia.privacycenter.data.ConsentPurposeItemDataModel

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
