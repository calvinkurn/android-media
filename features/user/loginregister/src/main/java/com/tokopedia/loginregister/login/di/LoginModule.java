package com.tokopedia.loginregister.login.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 10/10/18.
 */
@Module
public class LoginModule {
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @LoginScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }
}
