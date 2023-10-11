package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import android.widget.Toast
import com.gojek.kyc.plus.utils.KycSdkErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import javax.inject.Inject
import com.tokopedia.kyc_centralized.R

class GotoKycErrorHandler  @Inject constructor(): KycSdkErrorHandler {
    override fun onUserAuthenticationFailed(activity: Activity) {
        if (!activity.isFinishing) {
            val gotoLogin = RouteManager.getIntent(activity, ApplinkConstInternalUserPlatform.LOGIN)
            activity.startActivity(gotoLogin)
        }
    }

    override fun showNoInternetConnectionDialog(
        activity: Activity,
        ctaClickListener: () -> Unit,
        userDismissListener: () -> Unit
    ) {
        if (!activity.isFinishing) {
            Toast.makeText(activity.applicationContext, activity.getString(R.string.goto_kyc_internet_issue), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showSomethingWentWrongDialog(
        activity: Activity,
        title: String,
        description: String,
        buttonText: String,
        onClickCta: () -> Unit,
        userDismissListener: () -> Unit
    ) {
        if (!activity.isFinishing) {
            Toast.makeText(activity.applicationContext, activity.getString(R.string.goto_kyc_error_from_be), Toast.LENGTH_SHORT).show()
        }
    }
}
