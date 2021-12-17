package com.tokopedia.inactivephone_otp.testing.inactivephone.submitnewphone

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneSubmitDataActivity

class InactivePhoneSubmitDataIntentRule :
    IntentsTestRule<InactivePhoneSubmitDataActivity>(
        InactivePhoneSubmitDataActivity::class.java, false, false
    )