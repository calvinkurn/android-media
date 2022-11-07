package com.tokopedia.updateinactivephone.features.submitnewphone.regular

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.common.viewaction.simulateOnBackPressed
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkSubmitDataPageDisplayed
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkThumbnailImageIdCard
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkThumbnailImageSelfie
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.setPhoneNumberText
import org.junit.Test
import org.junit.runner.RunWith


@UiTest
@RunWith(AndroidJUnit4::class)
class RegularSubmitDataGeneralTest : BaseSubmitDataTest() {

    var phone = "084444123456"

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            submitDataUseCaseStub.response = inactivePhoneSubmitDataModel
        }
    }

    @Test
    fun show_submit_data_regular_page() {
        runTest {
            checkSubmitDataPageDisplayed()
        }
    }

    @Test
    fun input_valid_phone_number() {
        runTest {
            setPhoneNumberText(phone)
        }
    }

    @Test
    fun submit_new_phone() {
        runTest {
            val data = Intent().apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, "token123")
            }
            Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, data))
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
        }
    }

    @Test
    fun open_popup() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.checkPopupIsDisplayed()
        }
    }

    @Test
    fun on_click_exit_popup() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.clickOnButtonExitPopup()
        }
    }

    @Test
    fun on_click_lanjut_verifikasi() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.clickOnButtonLanjutVerifikasi()
        }
    }

    @Test
    fun check_thumbnail_id_card() {
        runTest {
            checkThumbnailImageIdCard()
        }
    }

    @Test
    fun check_thumbnail_selfie() {
        runTest {
            checkThumbnailImageSelfie()
        }
    }
}