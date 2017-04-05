package com.tokopedia.core.base.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(TActivity baseActivity);

    @ApplicationContext
    Context context();

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

    @AceQualifier
    Retrofit aceRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    @HadesQualifier
    Retrofit hadesRetrofit();

    Gson gson();

    @WsV4Qualifier
    Retrofit baseDomainRetrofit();

    @ActivityContext
    Context contextActivity();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();


}
