package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.widget.Toast
import com.gojek.kyc.plus.utils.KycSdkErrorHandler

//TODO: change implementation when OneKycSDK final was ready
class GotoKycErrorHandler: KycSdkErrorHandler {
    override fun onUserAuthenticationFailed(activity: Activity) {
        Toast.makeText(activity.applicationContext, "Launch Login flow", Toast.LENGTH_SHORT).show()
    }

    override fun showNoInternetConnectionDialog(
        activity: Activity,
        ctaClickListener: () -> Unit,
        userDismissListener: () -> Unit
    ) {
        Toast.makeText(activity.applicationContext, "No internet connection", Toast.LENGTH_SHORT).show()
    }

    override fun showSomethingWentWrongDialog(
        activity: Activity,
        title: String,
        description: String,
        buttonText: String,
        onClickCta: () -> Unit,
        userDismissListener: () -> Unit
    ) {
        Toast.makeText(activity.applicationContext, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
    }
}
