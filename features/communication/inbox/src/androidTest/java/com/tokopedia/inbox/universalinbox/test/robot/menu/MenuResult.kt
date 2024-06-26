package com.tokopedia.inbox.universalinbox.test.robot.menu

import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.inbox.R
import com.tokopedia.inbox.universalinbox.stub.common.atPositionCheckInstanceOf
import com.tokopedia.inbox.universalinbox.stub.common.withRecyclerView
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import org.hamcrest.CoreMatchers.not
import com.tokopedia.iconnotification.R as iconnotificationR

object MenuResult {

    fun assertNotificationCounter(counterText: String) {
        onView(withId(iconnotificationR.id.notification))
            .check(matches(withText(counterText)))
    }

    fun assertNotificationCounterGone() {
        onView(withId(iconnotificationR.id.notification))
            .check(matches(not(isDisplayed())))
    }

    fun assertMenuCounter(position: Int, counterText: String) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_notification_icon)
        ).check(matches(withText(counterText)))
    }

    fun assertMenuCounterGone(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_notification_icon)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertShopInfo(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_layout_shop_info)
        ).check(matches(isDisplayed()))
    }

    fun assertShopInfoGone(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_layout_shop_info)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    fun assertLabelMenu(position: Int) {
        onView(
            withRecyclerView(R.id.inbox_rv)
                .atPositionOnView(position, R.id.inbox_label_menu)
        ).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun assertSellerChatMenu(position: Int) {
        onView(withId(R.id.inbox_rv)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = UniversalInboxMenuUiModel::class.java
            )
        )
    }

    fun assertApplinkChatBuyer() {
        intended(
            hasData(
                "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_BUYER}"
            )
        )
    }

    fun assertApplinkChatSeller() {
        intended(
            hasData(
                "${ApplinkConst.TOP_CHAT}?${ApplinkConst.Inbox.PARAM_ROLE}=${ApplinkConst.Inbox.VALUE_ROLE_SELLER}"
            )
        )
    }

    fun assertApplinkDiscussion() {
        intended(
            hasData(
                ApplinkConstInternalGlobal.INBOX_TALK
            )
        )
    }

    fun assertApplinkReview() {
        intended(
            hasData(
                Uri.parse(ApplinkConst.REPUTATION).buildUpon().appendQueryParameter(
                    UniversalInboxValueUtil.PAGE_SOURCE_KEY,
                    UniversalInboxValueUtil.PAGE_SOURCE_REVIEW_INBOX
                ).build().toString()
            )
        )
    }
}
