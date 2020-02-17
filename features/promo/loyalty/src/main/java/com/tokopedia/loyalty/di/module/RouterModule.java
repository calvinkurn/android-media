package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 07/06/18.
 */
@Module
public class RouterModule {
    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }
}
