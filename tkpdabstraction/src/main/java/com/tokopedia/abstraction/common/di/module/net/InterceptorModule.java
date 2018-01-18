package com.tokopedia.abstraction.common.di.module.net;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@ApplicationContext Context context,
                                                   AbstractionRouter abstractionRouter,
                                                   UserSession userSession){
        return new TkpdAuthInterceptor(context, abstractionRouter, userSession);
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

}
