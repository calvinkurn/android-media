package com.tokopedia.mitra.common.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.mitra.account.fragment.MitraAccountFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@MitraScope
@Component(modules = MitraModule.class, dependencies = BaseAppComponent.class)
public interface MitraComponent {

    Retrofit.Builder retrofitBuilder();

    HttpLoggingInterceptor httpLoggingInterceptor();

    @ApplicationContext
    Context getContext();

    UserSession userSession();
}
