package com.tokopedia.vouchergame

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.product.CatalogOperatorAttributes
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.*
import com.tokopedia.vouchergame.common.view.model.VoucherGameExtraParam
import com.tokopedia.vouchergame.detail.view.activity.VoucherGameDetailActivity
import com.tokopedia.vouchergame.list.view.adapter.viewholder.VoucherGameListViewHolder
import com.tokopedia.vouchergame.test.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VoucherGameDetailActivityTest{
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var mActivityRule = ActivityTestRule(VoucherGameDetailActivity::class.java, false, false)

    @Before
    fun setUp(){
        Intents.init()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponseWithCheck {
            addMockResponse(
                    KEY_QUERY_VOUCHER_DETAIL,
                    InstrumentationMockHelper.getRawString(targetContext, R.raw.mock_response_recharge_product_input),
                    MockModelConfig.FIND_BY_CONTAINS)
        }
        val intent: Intent = VoucherGameDetailActivity.newInstance(targetContext, VoucherGameExtraParam(menuId = "4", operatorId = "2616"), CatalogOperatorAttributes(description = POKEMON_DETAIL_DESCRIPTION))
        mActivityRule.launchActivity(intent)
    }

    @Test
    fun validateTracking(){
        clickOnDetail()
        clickOnInfoButton()
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, targetContext, ANALYTICS_VOUCHER_GAME_DETAIL),
                hasAllSuccess())
    }

    fun clickOnDetail(){
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        if (itemCount() > 0) {
            Thread.sleep(3000)
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<VoucherGameListViewHolder>(0, ViewActions.click()))
            Thread.sleep(3000)
            Espresso.onView(ViewMatchers.withId(R.id.checkout_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    fun clickOnInfoButton(){
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_info_icon)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }

    fun itemCount(): Int {
        val itemCount: RecyclerView = mActivityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return itemCount.adapter?.itemCount ?: 0
    }

    @After
    fun tearDown(){
        Intents.release()
    }

    companion object {
        private const val KEY_QUERY_VOUCHER_DETAIL = "voucherGameProductDetail"
        private const val POKEMON_DETAIL_DESCRIPTION = "Pembelian ini dapat digunakan di Google Play Store, app store resmi Android untuk membeli item dalam game di Pokemon Go, serta lebih dari satu juta game dan aplikasi lainnya, dan banyak lagi. Untuk menukarkan, masukkan kode di aplikasi Play Store atau play.google.com. Untuk persyaratan lengkap silahkan lihat"
        private const val ANALYTICS_VOUCHER_GAME_DETAIL = "tracker/recharge/recharge_voucher_game_detail.json"
    }
}