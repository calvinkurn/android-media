package com.tokopedia.otp.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 4/24/18.
 */

@OtpScope
@Component(modules = OtpModule.class, dependencies = BaseAppComponent.class)
public interface OtpComponent {

    @ApplicationContext
    Context getApplicationContext();

    AnalyticTracker provideAnalyticTracker();

    Retrofit.Builder retrofitBuilder();

    OkHttpClient provideOkHttpClient();

    HttpLoggingInterceptor provideHttpLoggingInterceptor();

    UserSessionInterface provideUserSessionInterface();

    ChuckInterceptor provideChuckInterceptor();

    AbstractionRouter provideAbstractionRouter();

    TkpdAuthInterceptor tkpdAuthInterceptor();

    @MethodListQualifier
    OkHttpClient provideMethodListOkHttpClient();

}
