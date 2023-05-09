package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.view.View
import com.gojek.kyc.plus.card.KycPlusCard
import com.gojek.kyc.plus.card.KycPlusCardFactory

class GotoKycDefaultCard : KycPlusCardFactory {
    override fun getDialogCard(
        activity: Activity,
        contentView: View,
        onDismiss: (() -> Unit)?
    ): KycPlusCard {
        return GotoKycSdkBottomSheet(
            activity,
            view = contentView,
            onDismiss = onDismiss,
            showCloseIcon = true
        )
    }

    override fun getDismissibleNotchCard(
        activity: Activity,
        contentView: View,
        resizable: Boolean,
        onDismiss: (() -> Unit)?
    ): KycPlusCard {
        return GotoKycSdkBottomSheet(
            activity,
            view = contentView,
            onDismiss = onDismiss,
            showCloseIcon = true
        )
    }

    override fun getFixedCard(
        activity: Activity,
        contentView: View,
        isModal: Boolean,
        onUserDismiss: (() -> Unit)?
    ): KycPlusCard {
        return GotoKycSdkBottomSheet(
            activity,
            view = contentView,
            onDismiss = onUserDismiss,
            showCloseIcon = false
        )
    }
}
