package com.tokopedia.pms

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.imagepicker.common.PICKER_RESULT_PATHS
import com.tokopedia.pms.analytics.PmsIdlingResource
import com.tokopedia.pms.analytics.actionTest
import com.tokopedia.pms.paymentlist.domain.data.BankInfo
import com.tokopedia.pms.paymentlist.domain.data.BankTransferPaymentModel
import com.tokopedia.pms.proof.view.UploadProofPaymentActivity
import com.tokopedia.pms.test.R
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class UploadPaymentProofActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(UploadProofPaymentActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        Intents.init()
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        IdlingRegistry.getInstance().register(PmsIdlingResource.getIdlingResource())
        login()
        launchActivity()
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(PmsIdlingResource.getIdlingResource())
    }

    private fun stubImagePicker() {
        intending(IntentMatchers.anyIntent())
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    createImagePickerManual()
                )
            )
    }

    private fun createImagePickerManual(): Intent {
        val resultData = Intent()
        resultData.putStringArrayListExtra(PICKER_RESULT_PATHS, arrayListOf("a"))
        return resultData
    }

    @Test
    fun validateUploadPaymentProofEvents() {
        stubImagePicker()
        Thread.sleep(3000)

        actionTest {
            // Select Image Event
            actionClickView(R.id.button_save)
            Thread.sleep(3000)
            // Confirm Selected Image Event
            actionClickView(R.id.button_save)

        } assertTest {
            validate(cassavaTestRule, PAYMENT_UPLOAD_PROOF_TRACKER_PATH)
            finishTest()
        }
    }

    private fun login() = InstrumentationAuthHelper.loginInstrumentationTestUser1()

    private fun finishTest() {
        Intents.release()
        activityRule.activity.finish()
    }

    private fun launchActivity() {
        val model = BankTransferPaymentModel(
            "11", "11",
            323, "", 1, false, "p",
            BankInfo("", "", ""),
            BankInfo("", "", "")
        )
        val intent = UploadProofPaymentActivity.createIntent(context, model)
        activityRule.launchActivity(intent)
    }

    companion object {
        const val PAYMENT_UPLOAD_PROOF_TRACKER_PATH =
            "tracker/payment/pms/pms_upload_proof_tracking.json"
    }
}