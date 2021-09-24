package com.tokopedia.updateinactivephone.features.onboarding.withpin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.EXPEDITED
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.SOURCE_INACTIVE_PHONE
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneRegularActivity
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneSubmitDataActivity

class InactivePhoneOnboardingPinFragment: BaseInactivePhoneOnboardingFragment() {

    private var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null
    private var tracker = InactivePhoneWithPinTracker()

    override fun initInjector() { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            inactivePhoneUserDataModel = it.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTitle(getString(R.string.text_onboarding_title))
        updateDescription(getString(R.string.pin_onboarding_description))
    }

    override fun onButtonNextClicked() {
        tracker.clickOnButtonNextOnboarding()
        gotoChallengeEmail()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_OTP_EMAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    val isUseRegularFlow = data?.extras?.getBoolean(IS_USE_REGULAR_FLOW).orFalse()
                    inactivePhoneUserDataModel?.validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    if (isUseRegularFlow) {
                        gotoRegularFlow()
                    } else {
                        gotoChallengePin()
                    }
                }
            }

            REQUEST_CODE_VALIDATE_PIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val isUseRegularFlow = data?.extras?.getBoolean(IS_USE_REGULAR_FLOW).orFalse()
                    inactivePhoneUserDataModel?.validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    if (isUseRegularFlow) {
                        gotoRegularFlow()
                    } else {
                        gotoSubmitNewPhoneNumberPage()
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun gotoChallengeEmail() {
        val intent = goToVerification(InactivePhoneConstant.OTP_TYPE_INACTIVE_PHONE_EMAIL)
        startActivityForResult(intent, REQUEST_CODE_OTP_EMAIL)
    }

    private fun gotoChallengePin() {
        val intent = goToVerification(InactivePhoneConstant.OTP_TYPE_INACTIVE_PHONE_PIN)
        startActivityForResult(intent, REQUEST_CODE_VALIDATE_PIN)
    }

    private fun gotoRegularFlow() {
        context?.let {
            inactivePhoneUserDataModel?.let { data ->
                val intent = InactivePhoneRegularActivity.createIntent(it, data)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun gotoSubmitNewPhoneNumberPage() {
        activity?.let {
            startActivity(InactivePhoneSubmitDataActivity.getIntent(it, EXPEDITED, inactivePhoneUserDataModel))
            it.finish()
        }
    }

    private fun goToVerification(otpType: Int): Intent {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID_ENC, inactivePhoneUserDataModel?.userIdEnc.orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, inactivePhoneUserDataModel?.validateToken.orEmpty())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_INACTIVE_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType)
        return intent
    }

    companion object {

        private const val REQUEST_CODE_OTP_EMAIL = 101
        private const val REQUEST_CODE_VALIDATE_PIN = 102

        private const val IS_USE_REGULAR_FLOW = "isUseRegularFlow"

        fun instance(bundle: Bundle): Fragment {
            return InactivePhoneOnboardingPinFragment().apply {
                arguments = bundle
            }
        }
    }
}