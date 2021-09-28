package com.tokopedia.updateinactivephone.features.onboarding.regular

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment

class InactivePhoneOnboardingFragment : BaseInactivePhoneOnboardingFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTitle(getString(R.string.text_onboarding_title))
        updateDescription(getString(R.string.text_onboarding_description))
    }

    override fun onButtonNextClicked() {
        trackerRegular.clickOnNextButtonOnboarding()

        showLoading()
        gotoOnboardingIdCardPage()
    }

    private fun gotoOnboardingIdCardPage() {
        context?.let {
            (it as InactivePhoneRegularActivity).gotoOnboardingIdCard()
        }
    }

    private fun showLoading() {
        viewBinding?.loader?.show()
        viewBinding?.buttonNext?.isEnabled = false
    }
}