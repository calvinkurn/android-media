package com.tokopedia.inbox.universalinbox.test.robot.topads

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.atPositionCheckInstanceOf
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel

object TopAdsResult {

    fun assertHeadline(position: Int, reverse: Boolean = false) {
        onView(ViewMatchers.withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxTopadsHeadlineUiModel::class.java,
                reverse = reverse
            )
        )
    }
}
