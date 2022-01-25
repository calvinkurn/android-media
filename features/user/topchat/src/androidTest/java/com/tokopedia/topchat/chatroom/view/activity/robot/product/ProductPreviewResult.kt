package com.tokopedia.topchat.chatroom.view.activity.robot.product

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matcher

object ProductPreviewResult {

    fun verifyVariantLabel(@IdRes variantResourceId: Int, matcher: Matcher<View>, position: Int) {
        onView(
            withRecyclerView(R.id.rv_attachment_preview)
                .atPositionOnView(position, variantResourceId)
        ).check(matches(matcher))
    }

}