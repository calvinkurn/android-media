package com.tokopedia.inactivephone_otp.testing.inactivephone.onboarding.expedited

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.onboarding.withpin.InactivePhoneWithPinActivity

class InactivePhoneExpeditedOnboardingIntentRule :
    IntentsTestRule<InactivePhoneWithPinActivity>(
        InactivePhoneWithPinActivity::class.java, false, false
    )