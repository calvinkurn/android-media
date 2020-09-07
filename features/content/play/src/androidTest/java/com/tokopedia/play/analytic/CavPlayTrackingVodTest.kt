package com.tokopedia.play.analytic

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.play.R
import com.tokopedia.play.data.PlayVodMockModelConfig
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.util.clickOnViewChild
import com.tokopedia.play.util.orientationPortrait
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 07/09/20.
 * Instrumentation Test for checking Analytics Validator in Play Viewer Module
 *
 * Channel Type: VOD
 */
class CavPlayTrackingVodTest {

    /**
     * This class is an
     * extension of {@link ActivityTestRule}, which initializes Espresso-Intents before each test.
     * This rule also provides functional testing of a single Activity
     * the Activity under test will be launched before each test annotated with @Test
     * and before methods annotated with @Before
     *
     * @param initialTouchMode false because the Activity should not be placed into "touch mode" when started
     * @param launchActivity false because the Activity will be launched manually with intent extras
     **/
    @get:Rule
    val intentsTestRule = IntentsTestRule(PlayActivity::class.java, false, false)

    // context for the target application being instrumented
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    // database that holds all the analytics every user journey
    private val gtmLogDbSource = GtmLogDBSource(targetContext)

    /**
     * json file which holds valid trackers for Live Channel
     * file: libraries/analytics_debugger/src/main/assets/tracker/content/play/play_vod_analytic.json
     *
     * minus:
     * - rotate phone to full screen
     * - buffer
     **/
    private val fileName = "tracker/content/play/play_vod_analytic.json"

    @Before
    fun setUp() {
        // delete all data in the database
        gtmLogDbSource.deleteAll().subscribe()

        // setup mock response
        setupGraphqlMockResponse(PlayVodMockModelConfig())
    }

    @Test
    fun runValidateTracking() {
        // launch play activity with dummy channel id
        intentsTestRule.launchActivity(PlayActivity.createIntent(targetContext, "43215"))

        // idling resource: add artificial delays to the tests
        Thread.sleep(8000)

        // set login true because we need to test follow - unfollow, quick reply, and send chat
        InstrumentationAuthHelper.loginInstrumentationTestUser1(targetContext)

        // journey
        performCart()
        performLikeUnLike()
        performPinnedProduct()
        performRotateByClick()
        performClose()

        Thread.sleep(5000)

        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }

    private fun performCart() {
        Espresso.onView(ViewMatchers.withId(R.id.rl_cart)).perform(ViewActions.click()) // cart
        // fake intent activity, the destination activity will not be launched.
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun performLikeUnLike() {
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // like
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.v_like_click_area)).perform(ViewActions.click()) // unlike
    }

    private fun performPinnedProduct() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_pinned_action)).perform(ViewActions.click())
        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.rv_voucher_list)).perform(ViewActions.swipeLeft())
        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0, ViewActions.click())) // product detail
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0 , clickOnViewChild(R.id.btn_product_buy))) // buy without variant
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(0 , clickOnViewChild(R.id.iv_product_atc))) // atc without variant
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.snackbar_btn)).perform(ViewActions.click()) // lihat message ticker
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(1 , clickOnViewChild(R.id.btn_product_buy))) // buy => variant sheet
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.rv_variant))
                .perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1 , ViewActions.click()))
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click()) // buy with variant
        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<ProductLineViewHolder>(1 , clickOnViewChild(R.id.iv_product_atc))) // atc => variant sheet
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.rv_variant))
                .perform(RecyclerViewActions.actionOnItemAtPosition<VariantContainerViewHolder>(1 , ViewActions.click()))
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click()) // atc with variant
        Thread.sleep(2000)

        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.iv_sheet_close), ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
        Thread.sleep(1000)
    }

    private fun performRotateByClick() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_fullscreen_control)).perform(ViewActions.click()) // to landscape
        Espresso.onView(ViewMatchers.withId(R.id.view_video_settings)).perform(ViewActions.click())
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.isRoot()).perform(orientationPortrait())
        Thread.sleep(2000)
    }

    private fun performClose() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_back)).perform(ViewActions.click())
    }
}