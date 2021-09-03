package com.tokopedia.updateinactivephone.features.onboarding.regular

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneOnboardingFragment : BaseInactivePhoneOnboardingFragment() {

    override fun initInjector() {
        DaggerInactivePhoneComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .inactivePhoneModule(InactivePhoneModule(requireContext()))
            .build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTitle(getString(R.string.text_onboarding_title))
        updateDescription(getString(R.string.text_onboarding_description))
    }

    override fun onButtonNextClicked() {
        btnNext?.setOnClickListener {
            tracker.clickOnNextButtonOnboarding()

            showLoading()
            gotoOnboardingIdCardPage()
        }
    }

    private fun gotoOnboardingIdCardPage() {
        context?.let {
            (it as InactivePhoneRegularActivity).gotoOnboardingIdCard()
        }
    }

    private fun showLoading() {
        loader?.show()
        btnNext.isEnabled = false
    }
}