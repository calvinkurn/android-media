package com.tokopedia.mitra.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.ApplinkRouter;

import dagger.Module;
import dagger.Provides;

@Module
public class MitraModule {
    @Provides
    ApplinkRouter provideApplinkRouter(@ApplicationContext Context context) {
        if (context instanceof ApplinkRouter) {
            return (ApplinkRouter) context;
        }
        throw new RuntimeException("Application must implement " + ApplinkRouter.class.getSimpleName());
    }
}
