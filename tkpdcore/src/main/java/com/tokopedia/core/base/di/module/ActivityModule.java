package com.tokopedia.core.base.di.module;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author kulomady on 1/9/17.
 */
@Module
public class ActivityModule {
    private final ContextWrapper context;

    public ActivityModule(ContextWrapper context) {
        this.context = context;
    }
    @ApplicationScope
    @Provides
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}
