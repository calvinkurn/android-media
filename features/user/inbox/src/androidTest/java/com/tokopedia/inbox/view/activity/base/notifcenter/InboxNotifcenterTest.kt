package com.tokopedia.inbox.view.activity.base.notifcenter

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.viewmatcher.withRecyclerView
import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.notifcenter.DaggerFakeNotificationComponent
import com.tokopedia.inbox.view.activity.base.InboxTest

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

object NotifcenterAssertion {
    fun assertNotifWidgetMsg(position: Int, msg: String) {
        onView(
            withRecyclerView(R.id.recycler_view)
                .atPositionOnView(position, R.id.tp_message)
        ).check(matches(withText(msg)))
    }
}