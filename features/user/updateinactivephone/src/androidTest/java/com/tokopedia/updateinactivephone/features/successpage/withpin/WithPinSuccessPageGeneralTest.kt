package com.tokopedia.updateinactivephone.features.successpage.withpin

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.features.successpage.BaseSuccessPageTest
import com.tokopedia.updateinactivephone.features.successpage.InactivePhoneSuccessPageActivity
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkSuccessPageIsDisplayed
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.clickOnGotoHomeButton
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForExpedited
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isTitleForExpedited
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@MediumTest
@RunWith(AndroidJUnit4::class)
class WithPinSuccessPageGeneralTest : BaseSuccessPageTest() {

    @get:Rule
    var rule = IntentsTestRule(
        InactivePhoneSuccessPageActivity::class.java, false, false
    )

    @Test
    fun basic_test() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    null
                )
            )
            checkSuccessPageIsDisplayed()
            isTitleForExpedited("Nomor HP-mu sudah diubah!")
            isDescriptionForExpedited("Kamu bisa pakai nomor ini buat verifikasi atau masuk ke Tokopedia.")
            clickOnGotoHomeButton()
            intended(hasData(ApplinkConst.HOME))
        }
    }

}
