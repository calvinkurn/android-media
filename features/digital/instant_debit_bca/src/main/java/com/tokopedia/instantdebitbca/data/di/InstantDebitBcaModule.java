package com.tokopedia.instantdebitbca.data.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
@Module
public class InstantDebitBcaModule {

    @InstantDebitBcaScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
