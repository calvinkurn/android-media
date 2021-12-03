package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductPreviewResult {

    fun hasVariantLebelPreview(@IdRes variantResourceId: Int, variantText: String, position: Int) {
        onView(
            withRecyclerView(R.id.rv_attachment_preview)
                .atPositionOnView(position, variantResourceId)
        )
            .check(matches(withText(variantText)))
    }
}