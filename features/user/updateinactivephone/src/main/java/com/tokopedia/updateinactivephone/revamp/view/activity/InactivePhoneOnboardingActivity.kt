package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_ID_CARD
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_RECEIPT
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_SAVING_BOOK
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.OnboardingType.TYPE_SELFIE_AND_ID_CARD
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneOnbaordingFragment

class InactivePhoneOnboardingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val type = intent?.extras?.getString(KEY_ONBOARDING_TYPE) ?: ""
        if (type.isNotEmpty()) {
            when(type) {
                TYPE_ID_CARD -> {
                    return InactivePhoneOnbaordingFragment.createOnboardingIdCardPage()
                }
                TYPE_SELFIE_AND_ID_CARD -> {
                    return InactivePhoneOnbaordingFragment.createOnboardingSelfiePage()
                }
                TYPE_SAVING_BOOK -> {
                    return InactivePhoneOnbaordingFragment.createOnboardingSavingBookPage()
                }
                TYPE_RECEIPT -> {
                    return InactivePhoneOnbaordingFragment.createOnboardingReceiptPage()
                }
            }
        }
        return null
    }

    companion object {
        private const val KEY_ONBOARDING_TYPE = "onboardingType"

        fun getIntentIdCardPage(context: Context): Intent {
            return getIntent(context, TYPE_ID_CARD)
        }

        fun getIntentSelfiePage(context: Context): Intent {
            return getIntent(context, TYPE_SELFIE_AND_ID_CARD)
        }

        fun getIntentIdSavingBookPage(context: Context): Intent {
            return getIntent(context, TYPE_SAVING_BOOK)
        }

        fun getIntentReceiptPage(context: Context): Intent {
            return getIntent(context, TYPE_RECEIPT)
        }

        private fun getIntent(context: Context, type: String): Intent {
            val intent = Intent(context, InactivePhoneOnboardingActivity::class.java)
            intent.putExtra(KEY_ONBOARDING_TYPE, type)
            return intent
        }
    }
}
