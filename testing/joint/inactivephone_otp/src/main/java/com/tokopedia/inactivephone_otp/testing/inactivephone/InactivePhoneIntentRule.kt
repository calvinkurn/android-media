package com.tokopedia.inactivephone_otp.testing.inactivephone

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.InactivePhoneActivity

class InactivePhoneIntentRule :
    IntentsTestRule<InactivePhoneActivity>(
        InactivePhoneActivity::class.java, false, false
    )