package com.tokopedia.abstraction.common.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
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

    @ApplicationScope
    @Provides
    UserSession provideUserSession(AbstractionRouter abstractionRouter){
        return abstractionRouter.getSession();
    }

    @ApplicationScope
    @Provides
    AbstractionRouter provideAbstractionRouter(@ApplicationContext Context context){
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context);
        }else{
            return null;
        }
    }

    @ApplicationScope
    @Provides
    CacheManager provideGlobalCacheManager(AbstractionRouter abstractionRouter){
        return abstractionRouter.getGlobalCacheManager();
    }

}
