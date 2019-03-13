package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.home.account.di.scope.EmailNotifScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Arrays;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

@EmailNotifScope
@Module
public class EmailNotificationModule {

    @EmailNotifScope
    @Provides
    public ErrorResponseInterceptor provideErrorResponseInterceptor() {
        return new ErrorResponseInterceptor(TkpdV4ResponseError.class);
    }

    @EmailNotifScope
    @Provides
    public com.tokopedia.user.session.UserSession provideUserSession(@ApplicationContext Context context){
        return new com.tokopedia.user.session.UserSession(context);
    }

    @EmailNotifScope
    @Provides
    public TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                          com.tokopedia.user.session.UserSession userSession){
        return new TkpdAuthInterceptor(context, (NetworkRouter)context.getApplicationContext(), userSession);
    }

    @EmailNotifScope
    @Provides
    public List<Interceptor> provideInterceptorList(ErrorResponseInterceptor errorResponseInterceptor,
                                                    TkpdAuthInterceptor tkpdAuthInterceptor){
        return Arrays.asList(tkpdAuthInterceptor, errorResponseInterceptor);
    }

    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
