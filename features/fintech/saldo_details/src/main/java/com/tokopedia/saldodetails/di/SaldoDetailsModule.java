package com.tokopedia.saldodetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

@Module
public class SaldoDetailsModule {

    @SaldoDetailsScope
    @Provides
    UserSession providesUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

}
