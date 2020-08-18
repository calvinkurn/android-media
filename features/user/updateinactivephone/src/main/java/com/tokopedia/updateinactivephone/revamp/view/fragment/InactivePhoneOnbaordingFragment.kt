package com.tokopedia.updateinactivephone.revamp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_ID_CARD
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_RECEIPT
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_SAVING_BOOK
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_SELFIE_AND_ID_CARD
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*

class InactivePhoneOnbaordingFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        var contentLayout = 0
        var buttonListener: View.OnClickListener? = null
        val type = arguments?.getString(KEY_ONBOARDING_TYPE) ?: ""
        if (type.isNotEmpty()) {
            when (type) {
                TYPE_ID_CARD -> {
                    contentLayout = R.layout.layout_onboarding_id_card
                    buttonListener = View.OnClickListener {

                    }
                }
                TYPE_SELFIE_AND_ID_CARD -> {
                    contentLayout = R.layout.layout_onboarding_selfie
                    buttonListener = View.OnClickListener {

                    }
                }
                TYPE_SAVING_BOOK -> {
                    contentLayout = R.layout.layout_onboarding_saving_book
                    buttonListener = View.OnClickListener {

                    }
                }
                TYPE_RECEIPT -> {
                    contentLayout = R.layout.layout_onboarding_receipt
                    buttonListener = View.OnClickListener {

                    }
                }
            }
        }

        contentContainer?.apply {
            layoutResource = contentLayout
            inflate()
        }

        btnNext?.setOnClickListener(buttonListener)
    }

    companion object {
        private const val KEY_ONBOARDING_TYPE = "onboardingType"

        fun createOnboardingIdCardPage(): Fragment {
            return createFragment(TYPE_ID_CARD)
        }

        fun createOnboardingSelfiePage(): Fragment {
            return createFragment(TYPE_SELFIE_AND_ID_CARD)
        }

        fun createOnboardingSavingBookPage(): Fragment {
            return createFragment(TYPE_SAVING_BOOK)
        }

        fun createOnboardingReceiptPage(): Fragment {
            return createFragment(TYPE_RECEIPT)
        }

        private fun createFragment(type: String): Fragment {
            val bundle = Bundle()
            bundle.putString(KEY_ONBOARDING_TYPE, type)

            val fragment = InactivePhoneOnbaordingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}