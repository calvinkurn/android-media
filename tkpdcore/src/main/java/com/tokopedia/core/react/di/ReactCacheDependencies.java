package com.tokopedia.core.react.di;

import android.content.Context;

import com.tokopedia.core.database.o2o.CartDbManager;
import com.tokopedia.core.react.data.ReactCacheRepositoryImpl;
import com.tokopedia.core.react.data.factory.ReactCacheFactory;
import com.tokopedia.core.react.domain.ReactCacheRepository;

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
