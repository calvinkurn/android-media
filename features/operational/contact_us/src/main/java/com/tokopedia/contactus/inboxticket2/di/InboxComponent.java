package com.tokopedia.contactus.inboxticket2.di;

import android.content.Context;

import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;

import javax.inject.Named;

import dagger.Component;

@InboxScope
@Component(modules = InboxModule.class)
public interface InboxComponent {

    @Named("InboxListPresenter")
    InboxBaseContract.InboxBasePresenter getTicketListPresenter();

    @Named("InboxDetailPresenter")
    InboxDetailContract.InboxDetailPresenter getInboxDetailPresenter();


    ProvideRatingContract.ProvideRatingPresenter getProvideRatingPresenter();

    Context getContext();
}
