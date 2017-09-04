package com.tokopedia.posapp.di;

import android.content.Context;

import com.tokopedia.posapp.react.datasource.ReactCacheRepositoryImpl;
import com.tokopedia.posapp.react.domain.ReactCacheRepository;
import com.tokopedia.posapp.react.factory.ReactCacheFactory;

/**
 * Created by okasurya on 8/25/17.
 */

public class ReactCacheDependencies {
    Context context;

    public ReactCacheDependencies(Context context) {
        this.context = context;
    }

    public ReactCacheRepository provideReactCacheRepository() {
        return new ReactCacheRepositoryImpl(provideReactCacheFactory());
    }

    private ReactCacheFactory provideReactCacheFactory() {
        return new ReactCacheFactory();
    }
}
