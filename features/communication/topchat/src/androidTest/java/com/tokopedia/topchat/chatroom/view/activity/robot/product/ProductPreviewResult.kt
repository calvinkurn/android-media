package com.tokopedia.topchat.chatroom.view.activity.robot.product

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

object ProductPreviewResult {

    fun verifyVariantLabel(@IdRes variantResourceId: Int, matcher: Matcher<View>, position: Int) {
        onView(
            withRecyclerView(R.id.rv_attachment_preview)
                .atPositionOnView(position, variantResourceId)
        ).check(matches(matcher))
    }

    fun isCloseableAt(position: Int) {
        closeButtonIs(position, isDisplayed())
    }

    fun isNotCloseableAt(position: Int) {
        closeButtonIs(position, not(isDisplayed()))
    }

    private fun closeButtonIs(position: Int, matcher: Matcher<View>) {
        onView(
                withRecyclerView(R.id.rv_attachment_preview)
                        .atPositionOnView(position, R.id.iv_close)
        ).check(matches(matcher))
    }

    fun isLoadingAt(position: Int) {
        loadingIs(position, isDisplayed())
    }

    fun isNotLoadingAt(position: Int) {
        loadingIs(position, not(isDisplayed()))
    }

    private fun loadingIs(position: Int, matcher: Matcher<View>) {
        onView(
                withRecyclerView(R.id.rv_attachment_preview)
                        .atPositionOnView(position, R.id.lu_product_preview)
        ).check(matches(matcher))
    }

    fun isErrorAt(position: Int) {
        errorIs(position, isDisplayed())
    }

    fun isNotErrorAt(position: Int) {
        errorIs(position, not(isDisplayed()))
    }

    private fun errorIs(position: Int, matcher: Matcher<View>) {
        onView(
                withRecyclerView(R.id.rv_attachment_preview)
                        .atPositionOnView(position, R.id.ll_retry_product_preview)
        ).check(matches(matcher))
    }

}