package com.tokopedia.abstraction.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.module.net.NetModule;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;


/**
 * @author kulomady on 1/9/17.
 */
@Module (includes = {NetModule.class})
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    public Context provideContext() {
        return context.getApplicationContext();
    }

}
