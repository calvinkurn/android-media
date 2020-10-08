package com.tokopedia.thankyou_native.presentation.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.thankyou_native.TkpdIdlingResource
import com.tokopedia.thankyou_native.TkpdIdlingResourceProvider
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoaderFragmentTest {
    @get:Rule
    val activityRule = IntentsTestRule(ThankYouPageActivity::class.java, false, false)
    var idlingResource: TkpdIdlingResource? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)


    @Before
    fun setup() {
        clearData()
        launchActivity()
        setupIdlingResource()
        login()
    }

    @Test
    fun check_purchase_events_after_thanks() {

        val purchaseranchIOQuery = "tracker/linker/purchase_branch_io.json"
        val purchaseAppsFlyerQuery = "tracker/temp_apps_flyer_events/temp_af_purchase_app_flyer.json"

        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, purchaseranchIOQuery), hasAllSuccess())
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, purchaseAppsFlyerQuery), hasAllSuccess())
    }

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("Purchase")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString(ThankYouPageActivity.ARG_PAYMENT_ID, "783359")
        bundle.putString(ThankYouPageActivity.ARG_MERCHANT, "tokopediatest")
        val intent = Intent(context, ThankYouPageActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
    }

    @After
    fun unregisterIdlingResource() {
        idlingResource?.let {
            IdlingRegistry.getInstance().unregister(it.countingIdlingResource)
        }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }
}