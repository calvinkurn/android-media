package com.tokopedia.contactus.inboxtickets.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.contactus.inboxtickets.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsFragment
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.TicketFragment
import dagger.Component

@InboxScope
@Component(
    modules = [InboxModule::class, ContactUsViewModelModule::class],
    dependencies = arrayOf(
        BaseAppComponent::class
    )
)
interface InboxComponent {
    fun getContext(): Context

    fun inject(activity: ContactUsProvideRatingActivity)
    fun inject(activity: InboxContactUsActivity)
    fun inject(fragment: InboxContactUsFragment)
    fun inject(activity: InboxDetailActivity)
    fun inject(fragment: TicketFragment)
}
