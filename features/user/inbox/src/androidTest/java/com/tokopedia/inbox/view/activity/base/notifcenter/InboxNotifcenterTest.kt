package com.tokopedia.inbox.view.activity.base.notifcenter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.notifcenter.DaggerFakeNotificationComponent
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import org.hamcrest.Matcher

abstract class InboxNotifcenterTest : InboxTest() {

    protected val inboxNotifcenterDep = InboxNotifcenterFakeDependency()

    override fun before() {
        super.before()
        setupNotifcenterDaggerComponent()
        notifcenterComponent!!.injectMembers(inboxNotifcenterDep)
        inboxNotifcenterDep.init()
    }

    override fun onBuildUri(uriBuilder: Uri.Builder) {
        uriBuilder.appendQueryParameter(
            ApplinkConst.Inbox.PARAM_PAGE,
            ApplinkConst.Inbox.VALUE_PAGE_NOTIFICATION
        )
    }

    private fun setupNotifcenterDaggerComponent() {
        notifcenterComponent = DaggerFakeNotificationComponent.builder()
            .fakeBaseAppComponent(baseComponent)
            .build()
    }

    protected fun setShowBottomNav(intent: Intent, isShow: Boolean) {
        val uri = intent.data?.buildUpon()
        uri?.appendQueryParameter(
            ApplinkConst.Inbox.PARAM_SHOW_BOTTOM_NAV, isShow.toString()
        )
        intent.data = uri?.build()
    }

}

/**
 * All user action goes here
 */
object NotifcenterAction {
    fun scrollNotificationToPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    fun clickFilterAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_filter)
                .atPositionOnView(position, R.id.chips_filter)
        ).perform(click())
    }
}

/**
 * All assertion goes here
 */
object NotifcenterAssertion {
    fun assertNotifWidgetMsg(position: Int, msg: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_message)
        ).check(matches(withText(msg)))
    }

    fun assertNotifWidgetVisibility(position: Int, visibilityMatcher: Matcher<in View>) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_message)
        ).check(matches(visibilityMatcher))
    }

    fun assertItemListSize(size: Int) {
        assertRecyclerviewItem(hasTotalItemOf(size))
    }

    fun assertRecyclerviewItem(matcher: Matcher<in View>) {
        onView(withId(R.id.recycler_view))
            .check(matches(matcher))
    }
}