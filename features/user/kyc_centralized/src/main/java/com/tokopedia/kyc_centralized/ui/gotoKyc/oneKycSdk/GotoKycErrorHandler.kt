package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.gojek.kyc.plus.utils.KycSdkErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.SomethingWrongBottomSheet
import javax.inject.Inject

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
        if (!activity.isFinishing && activity is AppCompatActivity) {
            val bottomSheet = SomethingWrongBottomSheet.newInstance(type = SomethingWrongBottomSheet.TAG_CONNECTION_ISSUE)
            bottomSheet.setOnClickRetryListener {
                ctaClickListener()
            }
            bottomSheet.setOnDismissListener {
                userDismissListener()
            }
            bottomSheet.show(activity.supportFragmentManager, SomethingWrongBottomSheet.TAG_CONNECTION_ISSUE)
        }
    }

    override fun showSomethingWentWrongDialog(
        activity: Activity,
        title: String,
        description: String,
        buttonText: String,
        retryable: Boolean,
        onClickCta: () -> Unit,
        userDismissListener: () -> Unit
    ) {
        if (!activity.isFinishing && activity is AppCompatActivity) {
            val bottomSheet = SomethingWrongBottomSheet.newInstance(
                type = SomethingWrongBottomSheet.TAG_SOMETHING_WRONG,
                title = title,
                description = description,
                button = buttonText
            )
            bottomSheet.setOnClickRetryListener {
                onClickCta()
            }
            bottomSheet.setOnDismissListener {
                userDismissListener()
            }
            bottomSheet.show(activity.supportFragmentManager, SomethingWrongBottomSheet.TAG_SOMETHING_WRONG)
        }
    }
}
