package com.tokopedia.play.uitest.robot

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.test.espresso.clickOnViewChild
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.espresso.waitUntilViewIsDisplayed
import com.tokopedia.play.R
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play.ui.promosheet.viewholder.MerchantVoucherNewViewHolder
import com.tokopedia.play.ui.view.carousel.viewholder.ProductCarouselViewHolder
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not
import com.tokopedia.globalerror.R as globalErrorRes
import com.tokopedia.play_common.R as commonR
import com.tokopedia.unifycomponents.R as unifyComponentsR
import com.tokopedia.iconnotification.R as iconNotificationR

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
class PlayActivityRobot(
    channelId: String,
    initialDelay: Long = 1000,
    isYouTube: Boolean = false,
    isErrorPage: Boolean = false,
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val intent = RouteManager.getIntent(
        context,
        "tokopedia://play/${channelId}"
    )
    val scenario = ActivityScenario.launch<PlayActivity>(intent)

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)

        waitUntilViewIsDisplayed(
            withId(
                if (!isYouTube) R.id.view_video
                else if (isErrorPage) R.id.fl_global_error
                else R.id.youtube_webview
            )
        )
        delay(initialDelay)
    }

    fun openProductBottomSheet() = chainable {
        Espresso.onView(
            withId(R.id.view_product_see_more)
        ).perform(ViewActions.click())
    }

    fun closeAnyBottomSheet() = chainable {
        Espresso.onView(
            withId(commonR.id.iv_sheet_close)
        ).perform(ViewActions.click())
    }

    fun scrollProductBottomSheet(position: Int) = chainable {
        Espresso.onView(
            withId(R.id.rv_product_list)
        ).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, ViewActions.click()
            )
        )
    }

    fun clickBuyPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, clickOnViewChild(R.id.btn_second)
            )
        )
    }

    fun clickAtcPinnedProductCarousel() = chainable {
        Espresso.onView(
            withId(R.id.rv_product_featured)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductCarouselViewHolder.PinnedProduct>(
                0, clickOnViewChild(R.id.btn_first)
            )
        )
    }

    fun clickToasterAction() = chainable {
        Espresso.onView(
            withId(unifyComponentsR.id.snackbar_btn)
        ).perform(
            ViewActions.click()
        )
    }

    fun wait(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
    }

    /**
     * Assertion
     */
    fun assertHasPinnedItemInCarousel(
        hasPinned: Boolean,
        productName: String? = null,
    ) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))
        val interaction = Espresso.onView(
            RecyclerViewMatcher(R.id.rv_product_featured)
                .atPosition(0)
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )

        if (!hasPinned || productName == null) return@chainable
        interaction.check(
            matches(
                hasDescendant(withText(containsString(productName)))
            )
        )
    }

    fun assertHasPinnedItemInProductBottomSheet(hasPinned: Boolean) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))
        Espresso.onView(
            RecyclerViewMatcher(R.id.rv_product_list)
                .atPosition(0)
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )
    }

    fun assertHasPinnedItemInProductBottomSheet(
        productName: String,
        hasPinned: Boolean,
    ) = chainable {
        val viewMatcher = hasDescendant(withText(containsString("Pin Dipasang")))

        Espresso.onView(
            allOf(
                withId(R.id.rv_product_list),
                hasDescendant(withText(containsString(productName)))
            )
        ).check(
            if (hasPinned) matches(viewMatcher)
            else matches(not(viewMatcher))
        )
    }

    fun assertShowCartIconInProductBottomSheet(isShown: Boolean) = chainable {
        val viewMatcher = allOf(
            withId(com.tokopedia.play_common.R.id.icon_notif_right),
            isDescendantOfA(withParent(withId(R.id.cl_product_sheet)))
        )

        Espresso.onView(viewMatcher)
            .check(
                matches(
                    if (isShown) isDisplayed()
                    else not(isDisplayed())
                )
            )
    }

    fun assertShowCartCountInProductBottomSheet(isShown: Boolean) = chainable {
        val viewMatcher = allOf(
            withId(iconNotificationR.id.notification),
            isDescendantOfA(withParent(withId(R.id.cl_product_sheet))),
            isDescendantOfA(withParent(withId(R.id.bottom_sheet_header))),
        )

        Espresso.onView(viewMatcher)
            .check(
                matches(
                    if (isShown) isDisplayed()
                    else not(isDisplayed())
                )
            )
    }

    fun assertCartCountInProductBottomSheet(count: String) = chainable {
        val viewMatcher = allOf(
            withId(com.tokopedia.iconnotification.R.id.notification),
            isDescendantOfA(withParent(withId(R.id.cl_product_sheet))),
            isDescendantOfA(withParent(withId(R.id.bottom_sheet_header))),
        )

        Espresso.onView(viewMatcher)
            .check(
                matches(withText(count))
            )
    }

    fun hasEngagement(isGame: Boolean) {
        RecyclerViewMatcher(R.id.rv_engagement_widget)
            .atPosition(0)
            .matches(hasDescendant(
                hasBackground(
                    if (isGame) com.tokopedia.play_common.R.drawable.bg_play_quiz_widget //QUIZ
                    else com.tokopedia.play_common.R.drawable.bg_play_voucher_widget)
                )
            )
    }

    fun clickEngagementWidget(position: Int) {
        Espresso.onView(
            withId(R.id.rv_engagement_widget)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<EngagementWidgetViewHolder>(
                position, ViewActions.click()
            )
        )
    }

    fun swipeEngagement(index: Int) {
        Espresso.onView(
            withId(R.id.rv_engagement_widget)
        ).perform(
            RecyclerViewActions.scrollToPosition<EngagementWidgetViewHolder>(index)
        )
    }

    fun hasVoucherInBottomSheet() {
        val child = hasMinimumChildCount(1)
        Espresso.onView(
            withId(R.id.rv_voucher_list)
        ).check(matches(child))
    }

    fun clickVoucherInBottomSheet(position: Int) {
        Espresso.onView(
            withId(R.id.rv_voucher_list)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<MerchantVoucherNewViewHolder>(
                position, ViewActions.click()
            )
        )
    }

    fun isErrorViewAvailable() {
        Espresso.onView(withId(R.id.container_global_error)).check(matches(isDisplayed()))
    }

    fun clickGlobalErrorCta() {
        Espresso.onView(withId(globalErrorRes.id.globalerrors_action)).perform(ViewActions.click())
    }

    fun clickExitError() {
        Espresso.onView(withId(R.id.img_back)).perform(ViewActions.click())
    }

    fun swipeChannel() {
        Espresso.onView(isRoot()).perform(ViewActions.swipeLeft())
    }

    fun endViewIsAvailable(title: String) {
        val viewMatcher = hasDescendant(withText(containsString(title)))
        Espresso.onView(
            withId(R.id.cl_play_live_ended)
        ).check(
            matches(viewMatcher)
        )
    }

    fun isPopupShown(isShown: Boolean = true) = chainable {
        Espresso.onView(
            withId(R.id.cl_parent_follow_sheet)
        ).check(matches(if(isShown) isDisplayed() else not(isDisplayed())))
    }

    fun clickPartnerNamePopup() = chainable {
        Espresso.onView(
            withId(R.id.cl_follow_container)
        ).perform(ViewActions.click())
    }

    fun clickFollow() = chainable {
        Espresso.onView(
            withId(R.id.btn_follow)
        ).perform(ViewActions.click())
    }

    fun openSharingBottomSheet() = chainable {
        Espresso.onView(
            withId(R.id.view_share_experience)
        ).perform(ViewActions.click())
    }

    fun openKebabBottomSheet() = chainable {
        Espresso.onView(
            withId(R.id.view_kebab_menu)
        ).perform(ViewActions.click())
    }

    /**
     * Comment
     */

    fun openCommentBottomSheet() = chainable {
        Espresso.onView(withId(R.id.view_vod_comment)).perform(ViewActions.click())
    }

    fun clickWithId(viewId: Int) = chainable {
        Espresso.onView(withId(viewId)).perform(ViewActions.click())
    }

    fun impressIsAvailable(viewId: Int) = chainable {
        Espresso.onView(withId(viewId)).check(matches(isDisplayed()))
    }

    fun typeInEditText(text: String = "test", viewId: Int) = chainable {
        Espresso.onView(withId(viewId)).perform(ViewActions.typeText(text))
    }

    private fun chainable(fn: () -> Unit): PlayActivityRobot {
        fn()
        return this
    }
}
