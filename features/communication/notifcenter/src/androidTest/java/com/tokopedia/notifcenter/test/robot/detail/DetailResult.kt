package com.tokopedia.notifcenter.test.robot.detail

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.common.withRecyclerView
import com.tokopedia.notifcenter.test.robot.general.GeneralResult.assertRecyclerviewItem
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.BigDividerViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.EmptyNotificationWithRecomViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.LoadMoreViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.NormalNotificationViewHolder
import com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.SectionTitleViewHolder
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
}
