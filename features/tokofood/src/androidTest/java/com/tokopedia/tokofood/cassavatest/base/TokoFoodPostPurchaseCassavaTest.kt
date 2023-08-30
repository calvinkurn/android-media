package com.tokopedia.tokofood.cassavatest.base

import com.tokopedia.tokochat.config.util.TokoChatConnection
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.BaseTokoFoodPostPurchaseTest
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.stub.common.util.clickStickyButtonButton
import com.tokopedia.tokofood.stub.common.util.isViewDisplayed
import com.tokopedia.tokofood.stub.common.util.onClick
import com.tokopedia.tokofood.stub.common.util.onIdView
import com.tokopedia.tokofood.stub.common.util.scrollTo
import org.hamcrest.MatcherAssert
import org.junit.Rule

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
        intendingIntent()
        clickStickyButtonButton(R.id.btnOrderTrackingSecondaryHelp)
        validate(CLICK_HELP_PATH)
    }

    protected fun clickAddToCartCta() {
        intendingIntent()
        clickStickyButtonButton(R.id.beliLagiButton)
        validate(CLICK_ADD_TO_CART_PATH)
    }

    protected fun clickCallIcon() {
        activityRule.activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        onIdView(R.id.icDriverCall).isViewDisplayed().onClick()
        validate(CLICK_CALL_ICON_PATH)
    }

    protected fun clickChatIcon() {
        intendingIntent()
        activityRule.activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        onIdView(R.id.icDriverChat).isViewDisplayed().onClick()
        validate(CLICK_CHAT_ICON_PATH)
    }

    protected fun clickCallDriverBottomSheet() {
        intendingIntent()
        activityRule.activity.scrollTo<DriverSectionUiModel>(recyclerViewId = R.id.rvOrderTracking)
        onIdView(R.id.icDriverCall).isViewDisplayed().onClick()

        onIdView(R.id.tokofood_btn_masking_phone_number).isViewDisplayed().onClick()

        closeBottomSheet()

        validate(CLICK_CALL_DRIVER_BOTTOMSHEET_PATH)
    }

    protected fun enableChatIcon() {
        TokoChatConnection.tokoChatConfigComponent!!.getRemoteConfig().setString(
            TokoChatConnection.TOKOCHAT_REMOTE_CONFIG,
            "true"
        )
    }
}
