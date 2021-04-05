package com.tokopedia.product.addedit.instrumenttest

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.mock.AddEditProductEditingMockResponseConfig
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performClick
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performDialogPrimaryClick
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performDialogSecondaryClick
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performPressBack
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performReplaceText
import com.tokopedia.product.addedit.utils.InstrumentedTestUtil.performScrollAndClick
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

class AddEditProductEditingAnalyticTest {

    companion object {
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_DETAIL = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_detail.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_DESCRIPTION = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_description.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_SHIPMENT = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_shipment.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_FINISH = "tracker/merchant/product_add_edit/edit/product_preview_page_click_finish.json"

        private const val PRODUCT_DETAIL_CLICK_CHOOSE_CATEGORIES = "tracker/merchant/product_add_edit/edit/product_detail_click_choose_categories.json"
        private const val PRODUCT_DETAIL_CLICK_WHOLESALE_TOGGLE = "tracker/merchant/product_add_edit/edit/product_detail_click_wholesale_toggle.json"
        private const val PRODUCT_DETAIL_CLICK_PREORDER_TOGGLE = "tracker/merchant/product_add_edit/edit/product_detail_click_preorder_toggle.json"
        private const val PRODUCT_DETAIL_PAGE_CLICK_CONTINUE = "tracker/merchant/product_add_edit/edit/product_detail_page_click_continue.json"

        private const val PRODUCT_DESCRIPTION_PAGE_CLICK_CHANGE_VARIANT = "tracker/merchant/product_add_edit/edit/product_description_page_click_change_variant.json"
        private const val PRODUCT_DESCRIPTION_PAGE_CLICK_CONTINUE = "tracker/merchant/product_add_edit/edit/product_description_page_click_continue.json"

        private const val PRODUCT_SHIPPING_PAGE_CLICK_INSURANCE_TOGGLE = "tracker/merchant/product_add_edit/edit/product_shipping_page_click_insurance_toggle.json"

        private const val PRODUCT_PREVIEW_PAGE_CLICK_BACK = "tracker/merchant/product_add_edit/edit/product_preview_page_click_back.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_IMAGE = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_image.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_VARIANT = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_variant.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_PROMOTION = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_promotion.json"
        private const val PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_STATUS = "tracker/merchant/product_add_edit/edit/product_preview_page_click_change_status.json"

        private const val PRODUCT_PROMOTION_PAGE_CLICK_BACK = "tracker/merchant/product_add_edit/edit/product_promotion_page_click_back.json"
        private const val PRODUCT_PROMOTION_PAGE_CLICK_SAVE = "tracker/merchant/product_add_edit/edit/product_promotion_page_click_save.json"
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
    private val SAMPLE_PRODUCT_ID = "000"

    @Before
    fun beforeTest() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()

        setupGraphqlMockResponse(AddEditProductEditingMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser2()

        val intent = createIntentEditProduct(SAMPLE_PRODUCT_ID)
        activityRule.launchActivity(intent)
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun testEditProductJourney1() {
        testEditDetail()
        testEditDescription()
        testEditShipment()
        performClick(R.id.tv_done)

        //stepper
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_DETAIL)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_DESCRIPTION)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_SHIPMENT)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_FINISH)
        //detail
        doAnalyticDebuggerTest(PRODUCT_DETAIL_CLICK_CHOOSE_CATEGORIES)
        doAnalyticDebuggerTest(PRODUCT_DETAIL_CLICK_WHOLESALE_TOGGLE)
        doAnalyticDebuggerTest(PRODUCT_DETAIL_CLICK_PREORDER_TOGGLE)
        doAnalyticDebuggerTest(PRODUCT_DETAIL_PAGE_CLICK_CONTINUE)
        //description
        doAnalyticDebuggerTest(PRODUCT_DESCRIPTION_PAGE_CLICK_CHANGE_VARIANT)
        doAnalyticDebuggerTest(PRODUCT_DESCRIPTION_PAGE_CLICK_CONTINUE)
        //shipment
        doAnalyticDebuggerTest(PRODUCT_SHIPPING_PAGE_CLICK_INSURANCE_TOGGLE)

        activityRule.activity.finish()
    }

    @Test
    fun testEditProductJourney2() {
        testEditPhoto()
        testEditVariant()
        testEditPromotion()
        testEditProductStatus()

        //stepper
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_BACK)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_IMAGE)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_VARIANT)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_PROMOTION)
        doAnalyticDebuggerTest(PRODUCT_PREVIEW_PAGE_CLICK_CHANGE_STATUS)

        //promotion
        doAnalyticDebuggerTest(PRODUCT_PROMOTION_PAGE_CLICK_BACK)
        doAnalyticDebuggerTest(PRODUCT_PROMOTION_PAGE_CLICK_SAVE)

        activityRule.activity.finish()
    }

    private fun testEditDetail() {
        performScrollAndClick(R.id.tv_start_add_edit_product_detail)
        performScrollAndClick(R.id.tv_category_picker_button)
        performPressBack()
        performScrollAndClick(R.id.su_wholesale)
        performScrollAndClick(R.id.switch_preorder)
        performReplaceText(R.id.tfu_duration, "2")
        performScrollAndClick(R.id.btn_submit)
    }

    private fun testEditDescription() {
        performScrollAndClick(R.id.tv_start_add_edit_product_description)
        performScrollAndClick(R.id.tvAddVariant)
        performPressBack()
        performDialogSecondaryClick()
        performScrollAndClick(R.id.btnSave)
    }

    private fun testEditShipment() {
        performScrollAndClick(R.id.tv_start_add_edit_product_shipment)
        performScrollAndClick(R.id.radio_optional_insurance)
        performScrollAndClick(R.id.radio_required_insurance)
        performScrollAndClick(R.id.btn_save)
    }

    private fun testEditPhoto() {
        performScrollAndClick(R.id.tv_start_add_edit_product_photo)
        performClickNextOnImagePicker()
        performClickNextOnImagePicker()
    }

    private fun testEditVariant() {
        performScrollAndClick(R.id.tv_start_add_edit_product_variant)
        performPressBack()
        performDialogSecondaryClick()
    }

    private fun testEditPromotion() {
        performScrollAndClick(R.id.tv_edit_product_promotion)
        performClick(com.tokopedia.product.manage.R.id.submitCashbackButton)
        performPressBack()
    }

    private fun testEditProductStatus() {
        performScrollAndClick(R.id.su_product_status)
        performScrollAndClick(R.id.su_product_status)
        performPressBack()
        performDialogPrimaryClick()
    }

    private fun performClickNextOnImagePicker() {
        Thread.sleep(500)
        Espresso.onView(CommonMatcher
                .firstView(withText("Lanjut")))
                .perform(click())
    }

    private fun createIntentEditProduct(productId: String): Intent {
        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                .build()

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