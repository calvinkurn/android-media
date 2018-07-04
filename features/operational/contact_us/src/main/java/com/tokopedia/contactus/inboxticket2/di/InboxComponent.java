package com.tokopedia.contactus.inboxticket2.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by pranaymohapatra on 19/06/18.
 */
@InboxScope
@Component(modules = InboxModule.class)
public interface InboxComponent {

    @Named("InboxListPresenter")
    InboxBaseContract.InboxBasePresenter getTicketListPresenter();

    @Named("InboxDetailPresenter")
    InboxDetailContract.InboxDetailPresenter getInboxDetailPresenter();

    Context getContext();

    SharedPreferences getSharePreferences();


}
