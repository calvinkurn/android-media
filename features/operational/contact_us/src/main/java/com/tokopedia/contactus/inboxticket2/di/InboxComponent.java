package com.tokopedia.contactus.inboxticket2.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;

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

    Context getContext();

    SharedPreferences getSharePreferences();


}
