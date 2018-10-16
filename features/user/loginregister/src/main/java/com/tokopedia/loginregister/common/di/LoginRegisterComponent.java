package com.tokopedia.loginregister.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author by nisie on 10/15/18.
 */
@LoginRegisterScope
@Component(modules = LoginRegisterModule.class, dependencies = BaseAppComponent.class)
public interface LoginRegisterComponent {

    @ApplicationContext
    Context getContext();

    Retrofit.Builder getRetrofitBuilder();

    CacheManager globalCacheManager();

    LoginRegisterApi provideLoginRegisterApi();

    UserSessionInterface provideUserSession();

}