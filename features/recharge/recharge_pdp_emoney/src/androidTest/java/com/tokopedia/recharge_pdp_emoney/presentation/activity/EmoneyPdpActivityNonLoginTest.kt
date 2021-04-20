package com.tokopedia.recharge_pdp_emoney.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.not
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.*

/**
 * @author by jessica on 16/04/21
 */
class EmoneyPdpActivityNonLoginTest {
    @get:Rule
    var mActivityRule = ActivityTestRule(EmoneyPdpActivity::class.java,
            false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun stubAllIntent() {
        Intents.init()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK,
                null))
    }

    @Test
    fun testNonLoginFlow() {
        //Setup intent cart page & launch activity
        setupGraphqlMockResponse(EmoneyPdpResponseConfig(isLogin = false))
        setUpLaunchActivity()


        Espresso.onView(withId(R.id.emoneyPdpTicker)).check(matches(not(isDisplayed())))
        clickPromoTabAndSalinPromo()
        scanEmoneyCard()
        clickOnFavNumberOnInputView()
        clickProductAndSeeProductDetail()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_DIGITAL_EMONEY_NON_LOGIN),
                hasAllSuccess())
    }

    private fun setUpLaunchActivity() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupGraphqlMockResponse(EmoneyPdpResponseConfig(isLogin = false))
        val intent = Intent(context, EmoneyPdpActivity::class.java).setData(
                Uri.parse("tokopedia://digital/form?category_id=34&menu_id=267&template=electronicmoney")
        )
        mActivityRule.launchActivity(intent)
        Thread.sleep(2000)
    }

    private fun scanEmoneyCard() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(createDummyCardResponse())
        Thread.sleep(2000)

        Espresso.onView(withId(R.id.emoneyHeaderViewCtaButton)).perform(click())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.emoneyHeaderViewCardNumber)).check(matches(withText("12345678910")))
        Espresso.onView(withId(R.id.emoneyHeaderViewCardBalance)).check(matches(withText("Rp130.000")))
    }

    private fun clickPromoTabAndSalinPromo() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK,
                null))

        Espresso.onView(withId(R.id.emoneyPdpTab)).check(matches(not(isDisplayed())))
        Espresso.onView(withId(R.id.title_component)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.emoneyPdpPromoListWidget)).check(matches(isDisplayed()))
        Espresso.onView(AllOf.allOf(
                withId(R.id.recycler_view_menu_component),
                isDescendantOfA(withId(R.id.emoneyPdpPromoListWidget))
        )).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                        0, click()
                )
        )
        Thread.sleep(2000)

        Espresso.onView(AllOf.allOf(
                withId(R.id.recycler_view_menu_component),
                isDescendantOfA(withId(R.id.emoneyPdpPromoListWidget))
        )).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                        0, CommonActions.clickChildViewWithId(R.id.btn_copy_promo)
                )
        )
        Thread.sleep(2000)
    }

    private fun clickOnFavNumberOnInputView() {
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasShortClassName("com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity")))
                .respondWith(createOrderNumberTypeManual())
        Espresso.onView(withId(R.id.text_field_input)).perform(click())
        Thread.sleep(2000)

        Espresso.onView(withId(R.id.emoneyHeaderViewCardNumber)).check(matches(withText("12345678910")))
        Espresso.onView(AllOf.allOf(withText("99999999"), withId(R.id.text_field_input))).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.text_field_icon_2)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.text_field_icon_2)).perform(click())
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.emoneyPdpPromoListWidget)).check(matches(isDisplayed()))
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.text_field_input)).perform(click())
        Thread.sleep(1000)
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = "99999999")
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
                TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun createDummyCardResponse(): Instrumentation.ActivityResult {
        val dummyCardData = DigitalCategoryDetailPassData.Builder()
        dummyCardData.clientNumber("12345678910")
        dummyCardData.productId("1010")
        dummyCardData.operatorId("1010")
        dummyCardData.categoryId("1010")
        dummyCardData.additionalETollLastBalance("Rp130.000")
        val resultData = Intent()
        resultData.putExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, dummyCardData.build())
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun clickProductAndSeeProductDetail() {
        Espresso.onView(withId(R.id.emoneyBuyWidget)).check(matches(not(isDisplayed())))
        Espresso.onView(withId(R.id.emoneyProductWidgetTitle)).check(matches(withText("Pilih Nominal")))
        Espresso.onView(AllOf.allOf(withId(R.id.emoneyProductTitle), withText("Rp 10.000"))).check(matches(isDisplayed()))
        Espresso.onView(AllOf.allOf(withId(R.id.emoneyProductPrice), withText("Rp10.000"))).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.emoneyProductListRecyclerView)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<EmoneyPdpProductViewHolder>(
                        0, click()
                )
        )
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.emoneyBuyWidget)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.txt_recharge_checkout_price)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.txt_recharge_checkout_price)).check(matches(withText("Rp10.000")))

        Espresso.onView(withId(R.id.emoneyProductListRecyclerView)).check(matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<EmoneyPdpProductViewHolder>(0,
                        CommonActions.clickChildViewWithId(R.id.emoneyProductSeeDetailText))
        )
        Thread.sleep(1000)
        Espresso.onView(AllOf.allOf(withId(R.id.emoneyBottomSheetProductTitle), withText("Rp 10.000"))).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.emoneyBottomSheetProductDescription)).check(matches(isDisplayed()))
        Espresso.onView(AllOf.allOf(withId(R.id.emoneyBottomSheetProductPrice), withText("Rp10.000"))).check(matches(isDisplayed()))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        const val ANALYTIC_VALIDATOR_DIGITAL_EMONEY_NON_LOGIN = "tracker/recharge/recharge_pdp_emoney/recharge_emoney_pdp_non_login_tracking.json"
    }

}