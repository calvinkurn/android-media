package com.tokopedia.inbox.view.activity.base.notifcenter

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.notifcenter.DaggerFakeNotificationComponent
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.notifcenter.di.module.CommonModule

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

}