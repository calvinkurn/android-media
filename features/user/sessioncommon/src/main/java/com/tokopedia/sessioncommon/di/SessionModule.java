package com.tokopedia.sessioncommon.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreference;
import com.tokopedia.sessioncommon.data.fingerprintpreference.FingerprintPreferenceManager;
import com.tokopedia.sessioncommon.network.AccountsBearerInterceptor;
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 10/16/18.
 */
@Module
public class SessionModule {

    public static final String SESSION_MODULE = "Session";

    @SessionCommonScope
    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

    @Named(SESSION_MODULE)
    @SessionCommonScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @SessionCommonScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        return (NetworkRouter) context;
    }

    @SessionCommonScope
    @Provides
    TkpdOldAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                      NetworkRouter networkRouter,
                                                      @Named(SESSION_MODULE) UserSessionInterface userSession) {
        return new TkpdOldAuthInterceptor(context, networkRouter, userSession);
    }

    @SessionCommonScope
    @Provides
    AccountsBearerInterceptor provideAccountsBearerInterceptor(@Named(SESSION_MODULE) UserSessionInterface userSessionInterface) {
        return new AccountsBearerInterceptor(userSessionInterface);
    }

    @SessionCommonScope
    @Provides
    FingerprintPreference provideFingerprintPreferenceManager(@ApplicationContext Context context) {
        return new FingerprintPreferenceManager(context);
    }

}
