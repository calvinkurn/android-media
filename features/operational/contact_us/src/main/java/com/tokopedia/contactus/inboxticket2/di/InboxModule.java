package com.tokopedia.contactus.inboxticket2.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tokopedia.contactus.inboxticket2.domain.usecase.CloseTicketByUserUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketDetailUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.InboxOptionUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostMessageUseCase2;
import com.tokopedia.contactus.inboxticket2.domain.usecase.PostRatingUseCase;
import com.tokopedia.contactus.inboxticket2.domain.usecase.SubmitRatingUseCase;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.contract.ProvideRatingContract;
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxDetailPresenterImpl;
import com.tokopedia.contactus.inboxticket2.view.presenter.InboxListPresenterImpl;
import com.tokopedia.contactus.inboxticket2.view.presenter.ProvideRatingFragmentPresenter;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user.session.UserSession;


import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class InboxModule {

    private final Context context;

    public InboxModule(Context context) {
        this.context = context;
    }

    @InboxScope
    @Provides
    Context provideContext() {
        return context;
    }

    @InboxScope
    @Provides
    SharedPreferences provideSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Named("InboxListPresenter")
    InboxBaseContract.InboxBasePresenter provideTicketListPresenter(GetTicketListUseCase useCase) {
        return new InboxListPresenterImpl(useCase);
    }

    @Provides
    @Named("InboxDetailPresenter")
    InboxDetailContract.InboxDetailPresenter provideInboxListPresenter(GetTicketDetailUseCase useCase,
                                                                       PostMessageUseCase messageUseCase,
                                                                       PostMessageUseCase2 messageUseCase2,
                                                                       PostRatingUseCase ratingUseCase, InboxOptionUseCase inboxOptionUseCase,
                                                                       SubmitRatingUseCase submitRatingUseCase,
                                                                       CloseTicketByUserUseCase closeTicketByUserUseCase) {
        return new InboxDetailPresenterImpl(useCase, messageUseCase, messageUseCase2, ratingUseCase,inboxOptionUseCase,submitRatingUseCase,closeTicketByUserUseCase);
    }

    @InboxScope
    @Provides
    ProvideRatingContract.ProvideRatingPresenter provideRatingPresenter(SubmitRatingUseCase submitRatingUseCase) {
        return new ProvideRatingFragmentPresenter(submitRatingUseCase);
    }

    @InboxScope
    @Provides
    UserSessionInterface provideUserSession(Context context) {
        return new UserSession(context);
    }
}
