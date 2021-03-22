package com.tokopedia.product.addedit.instrumenttest

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
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
    fun testLayout() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.tv_start_add_edit_product_detail)))
                .perform(ViewActions.click())

        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.tv_category_picker_button)))
                .perform(ViewActions.click())

        Thread.sleep(5000)
        activityRule.activity.finish()
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
}