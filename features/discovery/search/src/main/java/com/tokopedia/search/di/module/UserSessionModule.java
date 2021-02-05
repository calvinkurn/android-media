package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@Module
public class UserSessionModule {

    @SearchScope
    @Provides
    UserSessionInterface provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
