package com.tokopedia.topchat.chatroom.view.activity.robot.previewattachment

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.atPosition
import org.hamcrest.Matcher
import com.tokopedia.merchantvoucher.R as merchantvoucherR

object PreviewAttachmentResult {

    fun assertAttachmentPreview(
        matcher: Matcher<View>
    ) {
        onView(withId(R.id.rv_attachment_preview)).check(matches(matcher))
    }

    fun assertVoucherPreview(
        position: Int,
        matcher: Matcher<View>
    ) {
        onView(withId(R.id.rv_attachment_preview))
            .check(
                matches(
                    atPosition(
                        position,
                        merchantvoucherR.id.tvVoucherDesc,
                        matcher
                    )
                )
            )
    }
}
