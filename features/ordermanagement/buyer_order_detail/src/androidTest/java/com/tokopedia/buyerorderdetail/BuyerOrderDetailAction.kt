package com.tokopedia.buyerorderdetail

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.buyerorderdetail.cassava.BuyerOrderDetailTrackerValidationConstant
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertTrue
import java.util.concurrent.TimeUnit

class BuyerOrderDetailAction {
    private fun findBuyerOrderDetailFragment(activity: AppCompatActivity): BuyerOrderDetailFragment? {
        return activity.supportFragmentManager.fragments.find { it is BuyerOrderDetailFragment } as? BuyerOrderDetailFragment
    }

    private fun findSecondaryActionButtonBottomSheet(activity: AppCompatActivity): BottomSheetUnify? {
        return findBuyerOrderDetailFragment(activity)?.childFragmentManager?.fragments?.find {
            it is BottomSheetUnify &&
                    it.bottomSheetTitle.text == BuyerOrderDetailTrackerValidationConstant.SECONDARY_ACTION_BUTTON_BOTTOMSHEET_TITLE
        } as? BottomSheetUnify
    }

    private fun findFinishOrderConfirmationBottomSheet(activity: AppCompatActivity): BottomSheetUnify? {
        return findBuyerOrderDetailFragment(activity)?.childFragmentManager?.fragments?.find {
            it is BottomSheetUnify &&
                    it.bottomSheetTitle.text == BuyerOrderDetailTrackerValidationConstant.FINISH_ORDER_CONFIRMATION_BOTTOMSHEET_TITLE
        } as? BottomSheetUnify
    }

    private fun isSecondaryActionButtonBottomSheetVisible(activity: AppCompatActivity): Boolean {
        return findSecondaryActionButtonBottomSheet(activity)?.isVisible == true
    }

    private fun isFinishOrderConfirmationBottomSheetVisible(activity: AppCompatActivity): Boolean {
        return findFinishOrderConfirmationBottomSheet(activity)?.isVisible == true
    }

    private fun waitForCondition(timeout: Long = TimeUnit.SECONDS.toMillis(5), predicate: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (!predicate() && System.currentTimeMillis() - start <= timeout) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        }
    }

    private fun clickView(matcher: Matcher<View>) {
        onView(matcher).perform(click())
    }

    private fun isViewVisible(matcher: Matcher<View>): Boolean {
        return try {
            onView(matcher).check(matches(isDisplayingAtLeast(90)))
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun scrollThroughUntilViewVisible(activity: AppCompatActivity, matcher: Matcher<View>) {
        if (isViewVisible(matcher)) return
        activity.findViewById<RecyclerView>(R.id.rvBuyerOrderDetail)?.let {
            while (it.canScrollVertically(1)) {
                onView(allOf(withId(R.id.rvBuyerOrderDetail), isAssignableFrom(RecyclerView::class.java), isDisplayed()))
                        .perform(GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER))
                if (isViewVisible(matcher)) return
            }
        }
    }

    private fun clickSecondaryActionButton(buttonText: String) {
        val matcher = allOf(withId(R.id.tvBuyerOrderDetailSecondaryActionButton), withText(buttonText), isAssignableFrom(Typography::class.java))
        waitUntilViewVisible(matcher)
        clickView(matcher)
    }

    private fun scrollToCopyInvoice(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.icBuyerOrderDetailCopyInvoice)))
    }

    private fun scrollToSeeInvoice(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.tvBuyerOrderDetailSeeInvoice)))
    }

    private fun scrollToShopName(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.tvBuyerOrderDetailShopName)))
    }

    private fun scrollToFirstProduct(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.tvBuyerOrderDetailProductName)))
    }

    private fun scrollToFirstProductActionButton(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.btnBuyerOrderDetailBuyProductAgain)))
    }

    private fun scrollToShipmentTnC(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withText(startsWith(BuyerOrderDetailTrackerValidationConstant.TICKER_SHIPMENT_INFO_TNC_TEXT))))
    }

    private fun scrollToCopyAWB(activity: AppCompatActivity) {
        scrollThroughUntilViewVisible(activity, firstView(withId(R.id.icBuyerOrderDetailCopyAwb)))
    }

    private fun waitUntilViewVisible(matcher: Matcher<View>) {
        waitForCondition {
            isViewVisible(matcher)
        }
    }

    private fun waitUntilSeeDetailButtonVisible() {
        waitUntilViewVisible(firstView(withId(R.id.tvBuyerOrderDetailSeeDetail)))
    }

    private fun waitUntilSecondaryActionButtonVisible() {
        waitUntilViewVisible(withId(R.id.btnBuyerOrderDetailSecondaryActions))
    }

    private fun waitUntilToolbarChatIconVisible() {
        waitUntilViewVisible(firstView(withTagStringValue(BuyerOrderDetailFragment.CHAT_ICON_TAG)))
    }

    private fun waitUntilSecondaryActionButtonBottomSheetVisible(activity: AppCompatActivity) {
        waitForCondition {
            isSecondaryActionButtonBottomSheetVisible(activity)
        }
    }

    private fun waitUntilFinishOrderConfirmationBottomSheetVisible(activity: AppCompatActivity) {
        waitForCondition {
            isFinishOrderConfirmationBottomSheetVisible(activity)
        }
    }

    private fun openSecondaryActionButtonBottomSheet(activity: AppCompatActivity) {
        if (isFinishOrderConfirmationBottomSheetVisible(activity)) {
            closeBottomSheets(activity)
            waitUntilSecondaryActionButtonVisible()
        }
        if (!isSecondaryActionButtonBottomSheetVisible(activity)) {
            clickView(withId(R.id.btnBuyerOrderDetailSecondaryActions))
            waitUntilSecondaryActionButtonBottomSheetVisible(activity)
        }
    }

    private fun openFinishOrderConfirmationBottomSheet(activity: AppCompatActivity, fromPrimaryActionButton: Boolean) {
        if (!isFinishOrderConfirmationBottomSheetVisible(activity)) {
            if (fromPrimaryActionButton) {
                if (isSecondaryActionButtonBottomSheetVisible(activity)) {
                    closeBottomSheets(activity)
                }
                clickPrimaryActionButton()
                waitUntilFinishOrderConfirmationBottomSheetVisible(activity)
            } else {
                openSecondaryActionButtonBottomSheet(activity)
                clickSecondaryActionButton(BuyerOrderDetailTrackerValidationConstant.ACTION_BUTTON_FINISH_ORDER_TEXT)
            }
        }
    }

    private fun closeBottomSheets(activity: AppCompatActivity) {
        activity.supportFragmentManager.fragments.find { it is BuyerOrderDetailFragment }?.let {
            it.childFragmentManager.fragments.forEach { if (it is BottomSheetUnify) it.dismiss() }
        }
    }

    private fun clickToolbarChatIcon() {
        clickView(firstView(withTagStringValue(BuyerOrderDetailFragment.CHAT_ICON_TAG)))
    }

    private fun clickSeeDetail() {
        clickView(firstView(withId(R.id.tvBuyerOrderDetailSeeDetail)))
    }

    private fun clickCopyInvoice() {
        clickView(firstView(withId(R.id.icBuyerOrderDetailCopyInvoice)))
    }

    private fun clickSeeInvoice() {
        clickView(firstView(withId(R.id.tvBuyerOrderDetailSeeInvoice)))
    }

    private fun clickShopName() {
        clickView(firstView(withId(R.id.tvBuyerOrderDetailShopName)))
    }

    private fun clickProduct() {
        clickView(firstView(withId(R.id.tvBuyerOrderDetailProductName)))
    }

    private fun clickProductActionButton() {
        clickView(firstView(withId(R.id.btnBuyerOrderDetailBuyProductAgain)))
    }

    private fun clickShipmentTnC() {
        clickView(firstView(withText(startsWith(BuyerOrderDetailTrackerValidationConstant.TICKER_SHIPMENT_INFO_TNC_TEXT))))
    }

    private fun clickCopyAWB() {
        clickView(firstView(withId(R.id.icBuyerOrderDetailCopyAwb)))
    }

    private fun clickPrimaryActionButton() {
        clickView(firstView(withId(R.id.btnBuyerOrderDetailPrimaryActions)))
    }

    private fun clickSecondaryActionButton(activity: AppCompatActivity, buttonText: String) {
        openSecondaryActionButtonBottomSheet(activity)
        clickSecondaryActionButton(buttonText)
    }

    private fun clickPrimaryActionButtonOnFinishOrderConfirmationBottomSheet() {
        val matcher = withId(R.id.btnFinishOrderPrimary)
        waitUntilViewVisible(matcher)
        clickView(matcher)
    }

    private fun clickSecondaryActionButtonOnFinishOrderConfirmationBottomSheet() {
        val matcher = withId(R.id.btnFinishOrderSecondary)
        waitUntilViewVisible(matcher)
        clickView(matcher)
    }

    fun launchBuyerOrderDetailActivity(activityRule: ActivityTestRule<BuyerOrderDetailActivity>) {
        activityRule.launchActivity(BuyerOrderDetailActivity.createIntent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                BuyerOrderDetailTrackerValidationConstant.cartString,
                BuyerOrderDetailTrackerValidationConstant.orderId,
                BuyerOrderDetailTrackerValidationConstant.paymentId))
    }

    fun blockAllIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    fun testClickToolbarChatIcon() {
        waitUntilToolbarChatIconVisible()
        clickToolbarChatIcon()
    }

    fun testClickSeeDetail() {
        waitUntilSeeDetailButtonVisible()
        clickSeeDetail()
    }

    fun testClickSeeInvoice(activity: AppCompatActivity) {
        scrollToSeeInvoice(activity)
        clickSeeInvoice()
    }

    fun testClickCopyInvoice(activity: AppCompatActivity) {
        scrollToCopyInvoice(activity)
        clickCopyInvoice()
    }

    fun testClickShopName(activity: AppCompatActivity) {
        scrollToShopName(activity)
        clickShopName()
    }

    fun testClickProduct(activity: AppCompatActivity) {
        scrollToFirstProduct(activity)
        clickProduct()
    }

    fun testClickProductActionButton(activity: AppCompatActivity) {
        scrollToFirstProductActionButton(activity)
        clickProductActionButton()
    }

    fun testClickShipmentTnC(activity: AppCompatActivity) {
        scrollToShipmentTnC(activity)
        clickShipmentTnC()
    }

    fun testClickCopyAWB(activity: AppCompatActivity) {
        scrollToCopyAWB(activity)
        clickCopyAWB()
    }

    fun testClickPrimaryActionButton() {
        clickPrimaryActionButton()
    }

    fun testClickSecondaryActionButtonHelp(activity: AppCompatActivity) {
        clickSecondaryActionButton(activity, BuyerOrderDetailTrackerValidationConstant.ACTION_BUTTON_HELP_TEXT)
    }

    fun testClickSecondaryActionButtonRequestCancel(activity: AppCompatActivity) {
        clickSecondaryActionButton(activity, BuyerOrderDetailTrackerValidationConstant.ACTION_BUTTON_REQUEST_CANCEL_TEXT)
    }

    fun testClickSecondaryActionButtonRequestComplaint(activity: AppCompatActivity) {
        clickSecondaryActionButton(activity, BuyerOrderDetailTrackerValidationConstant.ACTION_BUTTON_REQUEST_COMPLAINT_TEXT)
    }

    fun testClickSecondaryActionButtonTrack(activity: AppCompatActivity) {
        clickSecondaryActionButton(activity, BuyerOrderDetailTrackerValidationConstant.ACTION_BUTTON_TRACK_TEXT)
    }

    fun testClickSecondaryActionButtonOnFinishOrderConfirmationBottomSheet(activity: AppCompatActivity, fromPrimaryActionButton: Boolean) {
        openFinishOrderConfirmationBottomSheet(activity, fromPrimaryActionButton)
        clickSecondaryActionButtonOnFinishOrderConfirmationBottomSheet()
    }

    fun testClickPrimaryActionButtonOnFinishOrderConfirmationBottomSheet(activity: AppCompatActivity, fromPrimaryActionButton: Boolean) {
        openFinishOrderConfirmationBottomSheet(activity, fromPrimaryActionButton)
        clickPrimaryActionButtonOnFinishOrderConfirmationBottomSheet()
    }

    infix fun validate(validation: BuyerOrderDetailValidator.() -> Unit) = BuyerOrderDetailValidator().apply(validation)
}

class BuyerOrderDetailMock {
    enum class BuyerOrderDetailMockResponse(val id: Int) {
        MOCK_RESPONSE_700(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_700),
        MOCK_RESPONSE_601(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_601),
        MOCK_RESPONSE_600(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_600),
        MOCK_RESPONSE_530(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_530),
        MOCK_RESPONSE_450(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_450),
        MOCK_RESPONSE_400(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_400),
        MOCK_RESPONSE_220(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_220),
        MOCK_RESPONSE_10(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_10),
        MOCK_RESPONSE_0(com.tokopedia.buyerorderdetail.test.R.raw.response_mock_data_order_0)
    }

    fun mockOrderDetail(mockResponse: BuyerOrderDetailMockResponse) {
        val mockModelConfig = object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("MPBOMDetail", InstrumentationMockHelper.getRawString(context, mockResponse.id), FIND_BY_CONTAINS)
                return this
            }
        }
        setupGraphqlMockResponse(mockModelConfig)
    }

    infix fun actionTest(action: BuyerOrderDetailAction.() -> Unit) = BuyerOrderDetailAction().apply(action)
}

class BuyerOrderDetailValidator {
    companion object {
        val queriesToValidate = arrayListOf<String>()
    }

    fun clearQueries() {
        queriesToValidate.clear()
    }

    fun addQueriesToValidate(vararg queries: String) {
        queriesToValidate.addAll(queries)
    }

    fun hasPassedAnalytics(rule: CassavaTestRule) {
        val errorMessage = StringBuilder()
        queriesToValidate.forEach {
            try {
                MatcherAssert.assertThat(rule.validate(it), hasAllSuccess())
            } catch (e: AssertionError) {
                if (errorMessage.isEmpty()) {
                    errorMessage.appendLine()
                            .append("Begin Buyer Order Detail Analytic Error Message")
                }
                errorMessage.appendLine()
                        .append("Error when validating query ${it.split("/").last()}")
                        .appendLine()
                        .append(e.message)
            }
        }
        assertTrue(errorMessage.toString(), errorMessage.isBlank())
    }
}

fun setupMock(mock: BuyerOrderDetailMock.() -> Unit) = BuyerOrderDetailMock().apply(mock)