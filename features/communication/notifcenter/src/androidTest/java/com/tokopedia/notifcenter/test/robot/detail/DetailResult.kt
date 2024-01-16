package com.tokopedia.notifcenter.test.robot.detail

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.matchesBackgroundColor
import com.tokopedia.notifcenter.stub.common.withRecyclerView
import com.tokopedia.notifcenter.test.robot.general.GeneralResult.assertRecyclerviewItem
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.BigDividerViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationWithRecomViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NormalNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NotifcenterTimelineHistoryViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.SectionTitleViewHolder
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.test.application.matcher.hasViewHolderItemAtPosition
import com.tokopedia.test.application.matcher.hasViewHolderOf
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object DetailResult {

    fun assertSectionTitleAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                SectionTitleViewHolder::class.java
            )
        )
    }

    fun assertLoadMoreAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                LoadMoreViewHolder::class.java
            )
        )
    }

    fun assertNormalNotificationAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                NormalNotificationViewHolder::class.java
            )
        )
    }

    fun assertDividerAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                BigDividerViewHolder::class.java
            )
        )
    }

    fun assertEmptyNotificationWithRecomAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                EmptyNotificationWithRecomViewHolder::class.java
            )
        )
    }

    fun assertEmptyNotificationAt(position: Int) {
        assertRecyclerviewItem(
            hasViewHolderItemAtPosition(
                position,
                EmptyNotificationViewHolder::class.java
            )
        )
    }

    fun assertDoesNotHaveLoadMore() {
        assertRecyclerviewItem(
            CoreMatchers.not(hasViewHolderOf(LoadMoreViewHolder::class.java))
        )
    }

    fun assertSectionTitleTextAt(position: Int, title: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.txt_section_title)
        ).check(matches(withText(title)))
    }

    fun assertLoadMoreTitle(position: Int, title: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.btn_loading)
        ).check(matches(withText(title)))
    }

    fun assertEmptyNotifStateWithRecomText(position: Int, @StringRes msgRes: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_empty_title)
        ).check(matches(withText(msgRes)))
    }

    fun assertEmptyNotifStateIllustrationImageVisibility(
        position: Int,
        visibilityMatcher: Matcher<in View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_empty_notification_title)
        ).check(matches(visibilityMatcher))
    }

    fun assertEmptyNotifStateIllustrationTitle(position: Int, @StringRes msgRes: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_empty_notification_title)
        ).check(matches(withText(msgRes)))
    }

    fun assertEmptyNotifStateIllustrationDescription(position: Int, @StringRes msgRes: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_empty_filter)
        ).check(matches(withText(msgRes)))
    }

    fun assertLabelVariant(position: Int) {
        onView(
            withRecyclerView(R.id.rv_carousel_product)
                .atPositionOnView(position, R.id.pvl_variant)
        )
            .check(matches(isDisplayed()))
    }

    fun assertLabelVariantNotDisplayed(position: Int) {
        onView(
            withRecyclerView(R.id.rv_carousel_product)
                .atPositionOnView(position, R.id.pvl_variant)
        )
            .check(matches(not(isDisplayed())))
    }

    fun assertNotifWidgetVisibility(position: Int, visibilityMatcher: Matcher<in View>) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_message)
        ).check(matches(visibilityMatcher))
    }

    fun assertNotifWidgetMsg(position: Int, msg: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_message)
        ).check(matches(withText(msg)))
    }

    fun assertNotificationTypeBanner(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.txt_notification_type)
        )
            .check(matches(withText("Promo")))
    }

    fun assertNotificationBannerTitle(position: Int, title: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.txt_notification_title)
        )
            .check(matches(withText(title)))
    }

    fun assertNotificationBannerCountDown(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        } else {
            matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_status)
        )
            .check(matcher)
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tu_countdown)
        )
            .check(matcher)
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_end_date)
        )
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertNotificationBannerExpiredDate(position: Int, isVisible: Boolean) {
        val matcher = if (isVisible) {
            matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        } else {
            matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE))
        }
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_status)
        )
            .check(matcher)
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tu_countdown)
        )
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_end_date)
        )
            .check(matcher)
    }

    fun assertBackgroundColor(position: Int, color: Int?) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notification_container)
        ).check(matches(matchesBackgroundColor(color)))
    }

    fun assertNotificationUnpinned(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.layout_pin_top)
        ).check(matches(not(isDisplayed())))

        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tv_pin_expired)
        ).check(matches(not(isDisplayed())))
    }

    fun assertNotificationPinned(position: Int, isShowCountDown: Boolean = false) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.layout_pin_top)
        ).check(matches(isDisplayed()))

        if (isShowCountDown) {
            onView(
                withRecyclerView(R.id.recycler_view)
                    .atPositionOnView(position, R.id.tv_pin_expired)
            ).check(matches(not(isDisplayed())))
        } else {
            onView(
                withRecyclerView(R.id.recycler_view)
                    .atPositionOnView(position, R.id.tv_pin_expired)
            ).check(matches(isDisplayed()))
        }
    }

    fun assertLabel(position: Int, text: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.lb_product_label)
        ).check(matches(isDisplayed()))

        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.lb_product_label)
        ).check(matches(withText(text)))
    }

    fun assertNotificationWidgetFeed(position: Int, title: String, description: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.txt_notification_title)
        ).check(matches(withText(title)))

        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_tv_feed_description)
        ).check(matches(withText(description)))
    }

    fun assertWidgetFeedSingle(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_iv_feed_rect_single)
        ).check(matches(isDisplayed()))
    }

    fun assertWidgetFeedMultiple(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_iv_feed_circle_one)
        ).check(matches(isDisplayed()))

        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_iv_feed_circle_one)
        ).check(matches(isDisplayed()))
    }

    fun assertWidgetNoImage(position: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_cl_feed_images)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertWidgetFeedToggleButton(position: Int, visibility: ViewMatchers.Visibility) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_tv_toggle_history)
        ).check(matches(withEffectiveVisibility(visibility)))
    }

    fun assertWidgetFeedRv(position: Int, size: Int) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.notifcenter_rv_feed_history)
        ).check(matches(hasTotalItemOf(size, NotifcenterTimelineHistoryViewHolder::class.java)))
    }
}
