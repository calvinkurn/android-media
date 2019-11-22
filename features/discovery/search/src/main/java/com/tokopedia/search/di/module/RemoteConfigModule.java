package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.Set;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class RemoteConfigModule {

    @SearchScope
    @Provides
    RemoteConfig provideRemoteConfig(@ApplicationContext Context context) {
        // TODO:: DO NOT COMMIT!!
        return new RemoteConfig() {
            @Override
            public Set<String> getKeysByPrefix(String prefix) {
                return null;
            }

            @Override
            public boolean getBoolean(String key) {
                return true;
            }

            @Override
            public boolean getBoolean(String key, boolean defaultValue) {
                return defaultValue;
            }

            @Override
            public byte[] getByteArray(String key) {
                return new byte[0];
            }

            @Override
            public byte[] getByteArray(String key, byte[] defaultValue) {
                return new byte[0];
            }

            @Override
            public double getDouble(String key) {
                return 0;
            }

            @Override
            public double getDouble(String key, double defaultValue) {
                return 0;
            }

            @Override
            public long getLong(String key) {
                return 0;
            }

            @Override
            public long getLong(String key, long defaultValue) {
                return 0;
            }

            @Override
            public String getString(String key) {
                return null;
            }

            @Override
            public String getString(String key, String defaultValue) {
                return null;
            }

            @Override
            public void setString(String key, String value) {

            }

            @Override
            public void fetch(Listener listener) {

            }
        };
    }
}
