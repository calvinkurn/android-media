package com.tokopedia.loginregister.login.di;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
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
public class LoginModule {
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @LoginScope
    @Provides
    @Named(LOGIN_CACHE)
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, LOGIN_CACHE);
    }

    @LoginScope
    @Provides
    CoroutineDispatcher provideMainDispatcher() {
        return Dispatchers.getMain();
    }

}
