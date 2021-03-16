package com.tokopedia.product.addedit.instrumenttest

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.mock.AddEditProductPreviewMockResponseConfig
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditProductPreviewAnalyticTest {

    @get:Rule
    var activityRule: IntentsTestRule<AddEditProductPreviewActivity> = IntentsTestRule(AddEditProductPreviewActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val SAMPLE_PRODUCT_ID = "000"

    @Before
    fun beforeTest() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(AddEditProductPreviewMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser2()
        val intent = createIntentEditProduct()
        activityRule.launchActivity(intent)
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun testEditProductJourney() {
        testEditDetail()
        testEditDescription()
        testEditShipment()
        Espresso.onView(CommonMatcher
                .firstView(withId(R.id.tv_done)))
                .perform(click())

        //stepper
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_preview_page_click_change_detail.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_preview_page_click_change_description.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_preview_page_click_change_shipment.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_preview_page_click_finish.json")
        //detail
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_detail_click_choose_categories.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_detail_click_wholesale_toggle.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_detail_click_preorder_toggle.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_detail_page_click_continue.json")
        //description
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_description_page_click_change_variant.json")
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_description_page_click_continue.json")
        //shipment
        doAnalyticDebuggerTest("tracker/merchant/product_add_edit/edit/product_shipping_page_click_insurance_toggle.json")

        activityRule.activity.finish()
    }

    private fun testEditShipment() {
        performScrollAndClick(R.id.tv_start_add_edit_product_shipment)
        performScrollAndClick(R.id.radio_optional_insurance)
        performScrollAndClick(R.id.radio_required_insurance)
        performScrollAndClick(R.id.btn_save)
    }

    private fun testEditDetail() {
        performScrollAndClick(R.id.tv_start_add_edit_product_detail)
        performScrollAndClick(R.id.tv_category_picker_button)
        pressBack()
        performScrollAndClick(R.id.su_wholesale)
        performScrollAndClick(R.id.switch_preorder)
        performReplaceText(R.id.tfu_duration, "2")
        performScrollAndClick(R.id.btn_submit)
    }

    private fun testEditDescription() {
        performScrollAndClick(R.id.tv_start_add_edit_product_description)
        performScrollAndClick(R.id.tvAddVariant)
        pressBack()
        performDialogSecondaryClick()
        performScrollAndClick(R.id.btnSave)
    }

    private fun performDialogSecondaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(withId(R.id.dialog_btn_secondary)))
                .perform(click())
    }

    private fun performDialogPrimaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(withId(R.id.dialog_btn_primary)))
                .perform(click())
    }

    private fun createIntentEditProduct(): Intent {
        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, SAMPLE_PRODUCT_ID)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                .build()

        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, AddEditProductPreviewActivity::class.java).also {
            it.data = applink
        }
    }

    private fun performScrollAndClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(withId(id)))
                .perform(scrollTo())

        Espresso.onView(CommonMatcher
                .firstView(withId(id)))
                .perform(click())
    }

    private fun performReplaceText(id: Int, text: String) {
        Espresso.onView(CommonMatcher
                .firstView(withId(id)))
                .perform(scrollTo())

        Espresso.onView(Matchers.allOf(withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(withId(id))))
                .perform(typeText(text), closeSoftKeyboard())
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