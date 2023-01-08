/***
 * Test cases are temporarily disabled
 */

//package com.tokopedia.product.addedit.instrumenttest
//
//import android.app.Activity
//import android.app.Instrumentation
//import android.content.Intent
//import androidx.test.espresso.Espresso
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers
//import androidx.test.espresso.intent.rule.IntentsTestRule
//import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.platform.app.InstrumentationRegistry
//import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
//import com.tokopedia.product.addedit.R
//import com.tokopedia.product.addedit.mock.AddEditProductAddingMockResponseConfig
//import com.tokopedia.product.addedit.stub.AddEditProductDraftActivityStub
//import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performClick
//import com.tokopedia.test.application.util.InstrumentationAuthHelper
//import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
//import com.tokopedia.test.application.util.setupGraphqlMockResponse
//import org.hamcrest.MatcherAssert
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class AddEditProductDraftAnalyticTest {
//
//    companion object {
//        private const val PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT = "tracker/merchant/product_add_edit/add/product_draft_page_click_add_product.json"
//        private const val PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "tracker/merchant/product_add_edit/add/product_draft_page_click_add_product_without_draft.json"
//        private const val PRODUCT_PREVIEW_PAGE_CLICK_BACK = "tracker/merchant/product_add_edit/add/product_preview_page_click_back.json"
//        private const val PRODUCT_DRAFT_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_draft_page_open.json"
//    }
//
//    @get:Rule
//    var activityRule: IntentsTestRule<AddEditProductDraftActivityStub> = IntentsTestRule(
//        AddEditProductDraftActivityStub::class.java, false, false)
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext
//
//    @Before
//    fun beforeTest() {
//        setupGraphqlMockResponse(AddEditProductAddingMockResponseConfig())
//
//        InstrumentationAuthHelper.loginInstrumentationTestUser2()
//        activityRule.launchActivity(Intent())
//    }
//
//    @After
//    fun afterTest() {
//        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
//    }
//
//    @Test
//    fun testDraftJourney() {
//        testAddDraft()
//        testAddDraftWithoutData()
//
//        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT)
//        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_CLICK_ADD_PRODUCT_WITHOUT_DRAFT)
//        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_BACK)
//        doAnalyticDebuggerTest(PRODUCT_DRAFT_PAGE_OPEN)
//    }
//
//    private fun testAddDraft() {
//        onView(withId(R.id.item_add_product)).perform(click())
//        onView(withText(R.string.label_draft_menu_add_product)).inRoot(isPlatformPopup()).perform(click())
//        pressBack()
//    }
//
//    private fun testAddDraftWithoutData() {
//        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, Intent()))
//        performClick(R.id.button_add_promo)
//        pressBack()
//    }
//
//    private fun pressBack() {
//        Espresso.pressBackUnconditionally()
//    }
//
//    private fun doAnalyticDebuggerTest(fileName: String) {
//    }
//}