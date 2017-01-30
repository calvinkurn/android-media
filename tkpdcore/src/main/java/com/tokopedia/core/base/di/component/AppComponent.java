package com.tokopedia.core.base.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.di.qualifier.AceQualifier;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.qualifier.BaseDomainQualifier;
import com.tokopedia.core.base.di.qualifier.MojitoQualifier;
import com.tokopedia.core.base.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.BaseActivity;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author kulomady on 1/9/17.
 */
@ApplicationScope
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(BaseActivity baseActivity);

    @ApplicationContext
    Context context();

    @TopAdsQualifier
    Retrofit topAdsRetrofit();

    @AceQualifier
    Retrofit aceRetrofit();

    @MojitoQualifier
    Retrofit mojitoRetrofit();

    Gson gson();

    @BaseDomainQualifier
    Retrofit baseDomainRetrofit();

    @ActivityContext
    Context contextActivity();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();


}
