package com.tokopedia.otp.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Component;
import okhttp3.OkHttpClient;
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

    CacheManager globalCacheManager();

    UserSession provideUserSession();

    Retrofit.Builder retrofitBuilder();

    OkHttpClient provideOkHttpClient();

}
