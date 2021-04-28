package com.tokopedia.shop.analyticvalidator.testcase

import android.app.Application
import android.view.View
import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 02/11/20.
 */
class ShopPageHeaderPlayWidgetAnalyticTest {

    companion object {

        private const val IDLING_RESOURCE = "play_widget_fake_login"
        private const val FILE_NAME = "tracker/shop/shop_page_header_play_widget.json"

    }

    @get:Rule
    var intentsTestRule: IntentsTestRule<ShopPageActivity> = IntentsTestRule(ShopPageActivity::class.java, false, false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    private val idlingResourceLogin = CountingIdlingResource(IDLING_RESOURCE)
    private val idlingResourceInit: IdlingResource by lazy {
        object : IdlingResource {
            override fun getName(): String = "prepare"

            private var callback: IdlingResource.ResourceCallback? = null

            override fun isIdleNow(): Boolean {
                val buttonSetup = intentsTestRule.activity.findViewById<CardView>(R.id.play_sgc_widget_container)
                val isIdle =  buttonSetup != null && buttonSetup.visibility == View.VISIBLE
                if (isIdle) callback?.onTransitionToIdle()
                return isIdle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                this.callback = callback
            }
        }
    }

    @Before
    fun setup() {
        GlobalConfig.APPLICATION_TYPE = 2

        IdlingPolicies.setMasterPolicyTimeout(1, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(1, TimeUnit.MINUTES)

        gtmLogDbSource.deleteAll().toBlocking().first()

//        mockLogin()
    }

    @Test
    fun openShopPageJourney() {
        /**
         * Need to be fixed later
        intentsTestRule.launchActivity(Intent().apply {
            putExtra(ShopParamConstant.EXTRA_SHOP_ID, "1959733")
        })
        IdlingRegistry.getInstance().register(idlingResourceInit)
        Espresso.onIdle()

        // click widget "Yuk Mulai"
        Espresso.onView(CommonMatcher.firstView(ViewMatchers.withId(R.id.container_lottie)))
                .perform(ViewActions.click())

        intentsTestRule.activity.finish()
        Thread.sleep(5000)

        validateTracker()
         */
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResourceLogin)
        IdlingRegistry.getInstance().unregister(idlingResourceInit)
    }

    private fun mockLogin() {
        InstrumentationAuthHelper.loginToAnUser(
                targetContext.applicationContext as Application,
                idlingResourceLogin,
                userName = "andhika.djaffri+1@tokopedia.com",
                password = "tokopedia789"
        )
        IdlingRegistry.getInstance().register(idlingResourceLogin)
        Espresso.onIdle()
    }

    private fun validateTracker() {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDbSource, targetContext, FILE_NAME),
                hasAllSuccess()
        )
    }
}