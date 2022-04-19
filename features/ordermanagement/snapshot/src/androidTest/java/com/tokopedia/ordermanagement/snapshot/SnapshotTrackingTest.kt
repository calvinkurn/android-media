package com.tokopedia.ordermanagement.snapshot

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_DETAIL_ID
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_ID
import com.tokopedia.ordermanagement.snapshot.test.R
import com.tokopedia.ordermanagement.snapshot.util.SnapshotIdlingResource
import com.tokopedia.ordermanagement.snapshot.view.activity.SnapshotActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by fwidjaja on 2/5/21.
 */
class SnapshotTrackingTest {

    companion object {
        private const val QUERY_SUMMARY_SNAPSHOT = "tracker/ordermanagement/snapshot_summary.json"
        private const val KEY_SNAPSHOT_QUERY = "GetOrderSnapshot"
    }

    @get:Rule
    var activityRule = ActivityTestRule(SnapshotActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        Intents.init()
        gtmLogDBSource.deleteAll().subscribe()

        setupGraphqlMockResponse {
            addMockResponse(KEY_SNAPSHOT_QUERY, InstrumentationMockHelper.getRawString(context, R.raw.response_mock_snapshot), MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(SnapshotIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_snapshot_summary() {
        IdlingRegistry.getInstance().register(SnapshotIdlingResource.countingIdlingResource)


        val i = Intent()
        i.putExtra(PARAM_ORDER_ID, "704550391")
        i.putExtra(PARAM_ORDER_DETAIL_ID, "1052539767")
        activityRule.launchActivity(i)

        val query = Utils.getJsonDataFromAsset(context, QUERY_SUMMARY_SNAPSHOT)
                ?: throw AssertionError("Validator Query not found")

        runBot {
            loading()
            clickShopArea()
            clickBtnLihatHalamanProduk()
        } submit {
            hasPassedAnalytics(gtmLogDBSource, query)
        }
    }
}