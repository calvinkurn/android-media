package com.tokopedia.loginregister.login.di;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper;
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting;
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography;
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils;
import com.tokopedia.loginregister.common.DispatcherProvider;
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialScope;

import org.jetbrains.annotations.NotNull;

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

    @LoginScope
    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    @Nullable
    Cryptography provideCryptographyUtils(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return new CryptographyUtils();
        }
        return null;
    }

    @LoginScope
    @Provides
    FingerprintSetting provideFingerprintSetting(@ApplicationContext Context context){
        return new FingerprintPreferenceHelper(context);
    }

    @LoginScope
    @Provides
    DispatcherProvider provideDispatcherProvider() {
        return new DispatcherProvider() {
            @NotNull
            @Override
            public CoroutineDispatcher io() {
                return Dispatchers.getIO();
            }

            @NotNull
            @Override
            public CoroutineDispatcher ui() {
                return Dispatchers.getMain();
            }
        };
    }
}
