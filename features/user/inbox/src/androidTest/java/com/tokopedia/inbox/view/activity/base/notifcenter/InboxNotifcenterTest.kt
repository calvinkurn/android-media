package com.tokopedia.inbox.view.activity.base.notifcenter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewassertion.hasHolderItemAtPosition
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.notifcenter.DaggerFakeNotificationComponent
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.NotificationTopAdsBannerViewHolder
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.core.IsInstanceOf

open class InboxNotifcenterTest : InboxTest() {

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

object NotifcenterAction {
    fun scrollNotificationToPosition(position: Int) {
        onView(withId(R.id.recycler_view)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }
}

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

    fun assertTdnExistAtPosition(position: Int) {
        onView(withId(R.id.recycler_view))
            .check(
                hasHolderItemAtPosition(
                    position, IsInstanceOf(NotificationTopAdsBannerViewHolder::class.java)
                )
            )
    }

    fun assertTdnNotExistAtPosition(position: Int) {
        onView(withId(R.id.recycler_view))
            .check(
                hasHolderItemAtPosition(
                    position, not(IsInstanceOf(NotificationTopAdsBannerViewHolder::class.java))
                )
            )
    }
}