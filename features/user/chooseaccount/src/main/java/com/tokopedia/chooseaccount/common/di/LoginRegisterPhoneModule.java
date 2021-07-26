package com.tokopedia.chooseaccount.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.chooseaccount.common.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 10/15/18.
 */
@Module
public class LoginRegisterPhoneModule {

    @LoginRegisterPhoneScope
    @Provides
    LoginPhoneNumberAnalytics provideLoginAnalytics() {
        return new LoginPhoneNumberAnalytics();
    }

    @LoginRegisterPhoneScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
