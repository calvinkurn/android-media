package com.tokopedia.search.robot

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck

internal class PrepareSearchRobot {

    private lateinit var activityRule: ActivityTestRule<SearchActivity>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    fun usingRule(activityRule: ActivityTestRule<SearchActivity>) {
        this.activityRule = activityRule
    }

    fun mockResponse() {
        setupGraphqlMockResponse {
            addMockResponse("SearchProduct", getRawString(context, R.raw.search_product_common_response), FIND_BY_CONTAINS)
        }
    }

    fun mockResponseWithCheck() {
        setupGraphqlMockResponseWithCheck {
            addMockResponse("SearchProduct", getRawString(context, R.raw.search_product_common_response), FIND_BY_CONTAINS)
        }
    }

    fun deleteAllTrackingRecord() {
        GtmLogDBSource(InstrumentationRegistry.getInstrumentation().targetContext).deleteAll().subscribe()
    }

    fun disableOnBoarding() {
        LocalCacheHandler(context, SearchConstant.FreeOngkir.FREE_ONGKIR_LOCAL_CACHE_NAME).also {
            it.putBoolean(SearchConstant.FreeOngkir.FREE_ONGKIR_SHOW_CASE_ALREADY_SHOWN, true)
            it.applyEditor()
        }
    }

    fun loginAsTopAdsUser() {
        loginInstrumentationTestTopAdsUser()
    }

    infix fun search(action: SearchRobot.() -> Unit) = SearchRobot(activityRule).apply { action() }
}

internal fun prepare(action: PrepareSearchRobot.() -> Unit) = PrepareSearchRobot().apply { action() }