package com.tokopedia.topchat.chatroom.view.activity.robot.broadcast

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

object BroadcastResult {

    fun assertBroadcastShown() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, TopChatRoomBroadcastUiModel::class.java)
        )
    }

    fun assertBroadcastSpamHandler() {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(0, BroadcastSpamHandlerUiModel::class.java)
        )
    }

    fun assertBroadcastCtaText(text: String, match: Boolean = true) {
        val matcher = if (match) {
            withText(text)
        } else {
            not(withText(text))
        }
        onView(withId(R.id.topchat_cta_broadcast_tv)).check(
            matches(matcher)
        )
    }

    fun assertBroadcastCtaLabel(isVisible: Boolean) {
        val matcher: ViewAssertion

        @ColorRes val color: Int
        if (isVisible) {
            matcher = matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            color = unifyprinciplesR.color.Unify_NN400
        } else {
            matcher = matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
            color = unifyprinciplesR.color.Unify_GN500
        }
        onView(withId(R.id.topchat_cta_broadcast_label)).check(matcher)
        onView(withId(R.id.topchat_cta_broadcast_tv)).check(
            matches(hasTextColor(color))
        )
    }

    fun assertBroadcastCampaignLabelAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.broadcast_campaign_label, matcher)
        }
    }

    fun assertBroadcastCampaignLabelDescAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_tv_countdown, matcher)
        }
    }

    fun assertBroadcastCampaignLabelCountdownAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcasst_timer_countdown, matcher)
        }
    }

    fun assertBroadcastCampaignLabelStartDateIconAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_icon_start_date_countdown, matcher)
        }
    }

    fun assertBroadcastCampaignLabelStartDateTextAt(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_tv_start_date_countdown, matcher)
        }
    }

    fun assertBroadcastTitleHandle(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.title_bc_handle, matcher)
        }
    }

    fun assertBroadcastBtnStopPromo(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.btn_stop_promo, matcher)
        }
    }

    fun assertBroadcastBtnFollowShop(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.btn_follow_shop, matcher)
        }
    }

    fun assertBroadcastTitleHandleNotExist(
        position: Int
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                R.id.title_bc_handle
            )
        ).check(doesNotExist())
    }

    fun assertBroadcastBtnStopPromoNotExist(
        position: Int
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                R.id.btn_stop_promo
            )
        ).check(doesNotExist())
    }

    fun assertBroadcastBtnFollowShopNotExist(
        position: Int
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                position,
                R.id.btn_follow_shop
            )
        ).check(doesNotExist())
    }

    fun assertBroadcastBanner(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.iv_banner, matcher)
        }
    }

    /**
     * New Broadcast
     */
    fun assertNewBroadcastBanner(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_iv_banner, matcher)
        }
    }

    fun assertNewBroadcastCountdown(
        position: Int,
        text: String
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_tv_countdown, withText(text))
        }
    }

    fun assertNewBroadcastMessage(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_tv_message, matcher)
        }
    }

    fun assertNewBroadcastPromoProductCarousel(
        position: Int,
        totalItem: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_single_product, not(isDisplayed()))
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_rv, isDisplayed())
        }
        onView(withId(R.id.topchat_chatroom_broadcast_promo_rv)).check(matches(withTotalItem(totalItem)))
    }

    fun assertNewBroadcastPromoProductCarouselItem(
        position: Int,
        @IdRes viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_promo_rv).atPositionOnView(
                position,
                viewId
            )
        ).check(matches(matcher))
    }

    fun assertNewBroadcastPromoProductSingle(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_single_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_rv, not(isDisplayed()))
        }
    }

    fun assertNewBroadcastPromoWithoutProduct(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_single_product, not(isDisplayed()))
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_rv, not(isDisplayed()))
        }
    }

    fun assertNewBroadcastFlashSaleProductCarousel(
        position: Int,
        totalItem: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_flashsale_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_flashsale_rv, isDisplayed())
        }
        onView(withId(R.id.topchat_chatroom_broadcast_flashsale_rv)).check(matches(withTotalItem(totalItem)))
    }

    fun assertNewBroadcastFlashSaleProductCarouselItem(
        position: Int,
        @IdRes viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.topchat_chatroom_broadcast_flashsale_rv).atPositionOnView(
                position,
                viewId
            )
        ).check(matches(matcher))
    }

    fun assertNewBroadcastFlashSaleWithoutProduct(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_flashsale_product, isDisplayed())
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_flashsale_rv, not(isDisplayed()))
        }
    }

    fun assertNewBroadcastVoucherHeader(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_broadcast_tv_voucher_header, matcher)
        }
    }

    fun assertNewBroadcastVoucherDesc(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_broadcast_tv_desc, matcher)
        }
    }

    fun assertNewBroadcastPromoVoucher(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_promo_voucher, matcher)
        }
    }

    fun assertNewBroadcastFlashSaleVoucher(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_flashsale_voucher, matcher)
        }
    }

    fun assertNewBroadcastVoucherCarousel(
        position: Int,
        totalItem: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_broadcast_rv_voucher, isDisplayed())
        }
        onView(withId(R.id.topchat_broadcast_rv_voucher)).check(matches(withTotalItem(totalItem)))
    }

    fun assertNewBroadcastVoucher(
        position: Int,
        @IdRes viewId: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.topchat_broadcast_rv_voucher).atPositionOnView(
                position,
                viewId
            )
        ).check(matches(matcher))
    }

    fun assertNewBroadcastSingleVoucher(
        position: Int
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_broadcast_single_voucher, isDisplayed())
        }
    }

    fun assertNewBroadcastTimeStamp(
        position: Int,
        matcher: Matcher<View>
    ) {
        generalResult {
            assertViewInRecyclerViewAt(position, R.id.topchat_chatroom_broadcast_tv_status, matcher)
        }
    }
}
