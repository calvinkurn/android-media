package com.tokopedia.thankyou_native.presentation.fragment


import android.content.Intent
import android.net.Uri
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.thankyou_native.TkpdIdlingResource
import com.tokopedia.thankyou_native.TkpdIdlingResourceProvider
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoaderFragmentTest {
    @get:Rule
    val activityRule = ActivityTestRule(ThankYouPageActivity::class.java, false , false)
    @get:Rule
    var cassavaRule = CassavaTestRule()
    var idlingResource: TkpdIdlingResource? = null

    @Before
    fun setup() {
        login()
        setupIdlingResource()
        launchActivity()
    }

    @Test
    fun check_purchase_events_after_thanks() {

        val purchaseranchIOQuery = "tracker/linker/purchase_branch_io.json"
        val purchaseAppsFlyerQuery = "tracker/temp_apps_flyer_events/temp_af_purchase_app_flyer.json"

        assertThat(cassavaRule.validate(purchaseAppsFlyerQuery), hasAllSuccess())
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("Purchase")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    private fun launchActivity() {
        val result = Intent("com.tokopedia.RESULT_ACTION", Uri.parse("tokopedia://payment/thankyou?payment_id=853304807&merchant=tokopedia"))
        activityRule.launchActivity(result)
    }



    @After
    fun unregisterIdlingResource() {
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }
}