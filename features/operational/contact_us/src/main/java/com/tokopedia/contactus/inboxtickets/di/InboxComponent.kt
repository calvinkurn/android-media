package com.tokopedia.contactus.inboxtickets.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.contactus.inboxtickets.view.activity.ContactUsProvideRatingActivity
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsFragment
import com.tokopedia.contactus.inboxtickets.view.ticket.TicketActivity
import com.tokopedia.contactus.inboxtickets.view.ticket.TicketFragment
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
    fun inject(activity: TicketActivity)
    fun inject(fragment: TicketFragment)
}
