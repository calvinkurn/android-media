package com.tokopedia.contactus.inboxticket2.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pranaymohapatra on 19/06/18.
 */
@Module
public class InboxModule {

    private final Context context;

    public InboxModule(Context context) {
        this.context = context;
    }

    @InboxScope
    @Provides
    public Context provideContext() {
        return context.getApplicationContext();
    }

    @InboxScope
    @Provides
    public SharedPreferences provideSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Named("InboxListPresenter")
    InboxBaseContract.InboxBasePresenter provideTicketListPresenter(GetTicketListUseCase useCase) {
        return new InboxListPresenterImpl(useCase);
    }
}
