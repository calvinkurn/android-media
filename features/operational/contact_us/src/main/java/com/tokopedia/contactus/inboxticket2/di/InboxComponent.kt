package com.tokopedia.contactus.inboxticket2.di

import android.content.Context
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract
import dagger.Component
import javax.inject.Named

@InboxScope
@Component(modules = [InboxModule::class])
interface InboxComponent {
    @Named("InboxListPresenter")
    fun getTicketListPresenter(): InboxBaseContract.InboxBasePresenter

    @Named("InboxDetailPresenter")
    fun getInboxDetailPresenter(): InboxDetailContract.Presenter

    fun getContext(): Context
}