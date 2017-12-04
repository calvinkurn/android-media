package com.tokopedia.abstraction.di.module.net;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.di.scope.ApplicationScope;
import com.tokopedia.abstraction.utils.AuthUtil;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    TkpdAuthInterceptor provideTkpdAuthInterceptor(@AuthKeyQualifier String authKey,
                                                   Context context,
                                                   @FreshAccessTokenQualifier String freshAccessToken,
                                                   AbstractionRouter abstractionRouter,
                                                   UserSession userSession){
        return new TkpdAuthInterceptor(authKey, context, freshAccessToken, abstractionRouter, userSession);
    }

    @AuthKeyQualifier
    @ApplicationScope
    @Provides
    String provideAuthKey(@ApplicationContext Context context){
        return AuthUtil.KEY.KEY_WSV4;
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

    @FreshAccessTokenQualifier
    @ApplicationScope
    @Provides
    String provideFreshToken(AbstractionRouter abstractionRouter){
        return abstractionRouter.getSession().getFreshToken();
    }
}
