package com.tokopedia.product.addedit.instrumenttest

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.presentation.activity.AddEditProductDraftActivity
import com.tokopedia.product.addedit.mock.AddEditProductEditingMockResponseConfig
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.deleteAllDraft
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditProductDraftAnalyticTest {

    companion object {
        private const val PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT = "tracker/merchant/product_add_edit/add/product_draft_page_click_add_product.json"
        private const val PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "tracker/merchant/product_add_edit/add/product_draft_page_click_add_product_without_draft.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_BACK = "tracker/merchant/product_add_edit/add/product_preview_page_click_back.json"
        private const val PRODUCT_DRAFT_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_draft_page_open.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<AddEditProductDraftActivity> = IntentsTestRule(AddEditProductDraftActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")

        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(AddEditProductEditingMockResponseConfig())
        deleteAllDraft()

        InstrumentationAuthHelper.loginInstrumentationTestUser2()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun testDraftJourney() {
        testAddDraft()
        testAddDraftWithoutData()

        activityRule.activity.finish()

        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT)
        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT_WITHOUT_DRAFT)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_BACK)
        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_OPEN)

    }

    private fun testAddDraft() {
        onView(withId(R.id.item_add_product)).perform(click())
        onView(withText(R.string.label_draft_menu_add_product)).inRoot(isPlatformPopup()).perform(click())
        pressBack()
    }

    private fun testAddDraftWithoutData() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
        onView(CommonMatcher
                .firstView(withId(R.id.button_add_promo)))
                .perform(click())
        pressBack()
    }

    private fun pressBack() {
        Espresso.pressBackUnconditionally()
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}