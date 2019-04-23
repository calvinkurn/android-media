package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.presentation.presenter.SearchPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchModule {

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @SearchScope
    @Provides
    SearchPresenter provideSearchPresenter(@ApplicationContext Context context) {
        return new SearchPresenter(context);
    }

    @SearchScope
    @Provides
    SearchTracking provideSearchTracking(@ApplicationContext Context context,
                                         UserSessionInterface userSessionInterface) {
        return new SearchTracking(context, userSessionInterface);
    }
}
