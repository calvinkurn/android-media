package com.tokopedia.product.addedit.instrumenttest

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.mock.AddEditProductAddingMockResponseConfig
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performDialogPrimaryClick
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performPressBack
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performReplaceText
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performScrollAndClick
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.getElementFromMatchAtPosition
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.constant.Constant
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddEditProductAddingAnalyticTest {

    companion object {
        private const val PRODUCT_PREVIEW_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_preview_page_open.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_START = "tracker/merchant/product_add_edit/add/product_preview_page_click_start.json"
        private const val PRODUCT_PREVIEW_PAGE_CANCEL_BACK = "tracker/merchant/product_add_edit/add/product_preview_page_cancel_back.json"

        private const val PRODUCT_IMAGE_PICKER_OPEN = "tracker/merchant/product_add_edit/add/product_image_picker_open.json"
        private const val PRODUCT_IMAGE_EDITOR_CLICK_CONTINUE = "tracker/merchant/product_add_edit/add/product_image_editor_click_continue.json"

        private const val PRODUCT_DETAIL_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_detail_page_open.json"
        private const val PRODUCT_DETAIL_PAGE_CLICK_BACK = "tracker/merchant/product_add_edit/add/product_detail_page_click_back.json"


        private const val PRODUCT_DETAIL_PAGE_CLICK_CONTINUE = "tracker/merchant/product_add_edit/add/product_detail_page_click_continue.json"

        private const val PRODUCT_DESCRIPTION_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_description_page_open.json"
        private const val PRODUCT_DESCRIPTION_PAGE_CLICK_CONTINUE = "tracker/merchant/product_add_edit/add/product_description_page_click_continue.json"

        private const val PRODUCT_SHIPPING_PAGE_OPEN = "tracker/merchant/product_add_edit/add/product_shipping_page_open.json"

    }

    @get:Rule
    var activityRule: IntentsTestRule<AddEditProductPreviewActivity> = IntentsTestRule(AddEditProductPreviewActivity::class.java, false, false)
    @get:Rule
    var permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()

        setupGraphqlMockResponse(AddEditProductAddingMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser2()

        val intent = createIntentAddProduct()
        activityRule.launchActivity(intent)
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun testAddProductJourney1() {
        testAddPhoto()
        performPressBack()
        performPressBack()
        performDialogPrimaryClick()

        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_OPEN)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_START)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CANCEL_BACK)

        doAnalyticDebuggerTest(PRODUCT_IMAGE_PICKER_OPEN)
        doAnalyticDebuggerTest(PRODUCT_IMAGE_EDITOR_CLICK_CONTINUE)

        doAnalyticDebuggerTest(PRODUCT_DETAIL_PAGE_OPEN)
        doAnalyticDebuggerTest(PRODUCT_DETAIL_PAGE_CLICK_BACK)

        activityRule.activity.finish()
    }

    @Test
    fun testAddProductJourney2() {
        testAddPhoto()
        testDetailProduct()
        testDescriptionProduct()
        testShipmentProduct()

        doAnalyticDebuggerTest(PRODUCT_DETAIL_PAGE_CLICK_CONTINUE)

        doAnalyticDebuggerTest(PRODUCT_DESCRIPTION_PAGE_OPEN)
        doAnalyticDebuggerTest(PRODUCT_DESCRIPTION_PAGE_CLICK_CONTINUE)

        doAnalyticDebuggerTest(PRODUCT_SHIPPING_PAGE_OPEN)

        activityRule.activity.finish()
    }

    private fun testAddPhoto() {
        performScrollAndClick(R.id.tv_start_add_edit_product_photo)
        Espresso.onView(getElementFromMatchAtPosition(withId(R.id.recycler_view), 1)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        performClickNextOnImagePicker()
        performClickNextOnImagePicker()
    }

    private fun testDetailProduct() {
        performReplaceText(R.id.tfu_product_name, "nama produk")
        performReplaceText(R.id.tfu_product_price, "10000")
        performScrollAndClick(R.id.btn_submit)
    }

    private fun testDescriptionProduct() {
        performReplaceText(R.id.textFieldDescription, "desc")
        performScrollAndClick(R.id.btnNext)
    }

    private fun testShipmentProduct() {
        performScrollAndClick(R.id.radio_optional_insurance)
        performScrollAndClick(R.id.radio_required_insurance)
        performScrollAndClick(R.id.btn_end)
    }

    private fun performClickNextOnImagePicker() {
        Thread.sleep(500)
        Espresso.onView(CommonMatcher
                .firstView(withText("Lanjut")))
                .perform(click())
    }

    private fun createIntentAddProduct(): Intent {
        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)

        return Intent(InstrumentationRegistry.getInstrumentation().targetContext, AddEditProductPreviewActivity::class.java).also {
            it.data = applink
        }
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}