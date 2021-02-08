package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import dagger.Module;
import dagger.Provides;

@Module
public class RemoteConfigModule {

    @SearchScope
    @Provides
    RemoteConfig provideRemoteConfig(@ApplicationContext Context context) {
        return new FirebaseRemoteConfigImpl(context);
    }
}
