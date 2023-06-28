package com.tokopedia.tokofood.stub.cassavatest.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.BaseTokoFoodPostPurchaseTest
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.stub.common.util.isViewDisplayed
import com.tokopedia.tokofood.stub.common.util.onClick
import com.tokopedia.tokofood.stub.common.util.onIdView
import com.tokopedia.tokofood.stub.common.util.scrollTo
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class TokoFoodPostPurchaseCassavaTest : BaseTokoFoodPostPurchaseTest() {

    companion object {
        const val CLICK_ADD_TO_CART_PATH =
            "tracker/tokofood/post_purchase/tokofood_post_purchase_click_add_to_cart.json"
        const val CLICK_CALL_DRIVER_BOTTOMSHEET_PATH =
            "tracker/tokofood/post_purchase/tokofood_post_purchase_click_call_driver_bottomsheet.json"
        const val CLICK_CALL_ICON_PATH =
            "tracker/tokofood/post_purchase/tokofood_post_purchase_click_call_icon.json"
        const val CLICK_CHAT_ICON_PATH =
            "tracker/tokofood/post_purchase/tokofood_post_purchase_click_chat_icon.json"
        const val CLICK_HELP_PATH =
            "tracker/tokofood/post_purchase/tokofood_post_purchase_click_help.json"
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected fun validate(fileName: String) {
        dismissPage()
        Thread.sleep(3000)
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    protected fun clickHelpCta() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.startActivity(getTokoFoodOrderTrackingActivityIntent())
        }
        onIdView(R.id.btnOrderTrackingSecondaryHelp).isViewDisplayed().onClick()
        validate(CLICK_HELP_PATH)
    }

    protected fun clickAddToCartCta() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.startActivity(getTokoFoodOrderTrackingActivityIntent())
        }
        onIdView(R.id.btnOrderDetailSecondaryActions).isViewDisplayed().onClick()
        validate(CLICK_ADD_TO_CART_PATH)
    }

    protected fun clickCallIcon() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.startActivity(getTokoFoodOrderTrackingActivityIntent())
            activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        }
        onIdView(R.id.icDriverCall).isViewDisplayed().onClick()
        validate(CLICK_CALL_ICON_PATH)
    }

    protected fun clickChatIcon() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        }
        onIdView(R.id.icDriverChat).isViewDisplayed().onClick()
        validate(CLICK_CHAT_ICON_PATH)
    }

    protected fun clickCallDriverBottomSheet() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.startActivity(getTokoFoodOrderTrackingActivityIntent())
            activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        }
        onIdView(R.id.icDriverChat).isViewDisplayed().onClick()

        onIdView(R.id.tokofood_btn_masking_phone_number).isViewDisplayed().onClick()

        validate(CLICK_CALL_DRIVER_BOTTOMSHEET_PATH)
    }
}
