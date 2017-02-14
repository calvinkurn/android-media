package com.tokopedia.core.base.di.module;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author kulomady on 1/9/17.
 */
@Module
public class ActivityModule {
    private final Activity context;

    public ActivityModule(Activity context) {
        this.context = context;
    }
    @ApplicationScope
    @Provides
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}
