package com.tokopedia.logintest.login.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers;
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * @author by nisie on 10/10/18.
 */
@Module
public class LoginTestAppModule {
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @LoginTestAppScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }

    @LoginTestAppScope
    @Provides
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

    @LoginTestAppScope
    @Provides
    CoroutineDispatchers provideCoroutineDispatcher() {
        return CoroutineDispatchersProvider.INSTANCE;
    }
}
