package com.tokopedia.abstraction.di.module.net;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.di.scope.ApplicationScope;

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
                                                   AbstractionRouter abstractionRouter){
        return new TkpdAuthInterceptor(authKey, context, freshAccessToken, abstractionRouter);
    }

    @AuthKeyQualifier
    @ApplicationScope
    @Provides
    String provideAuthKey(Context context){
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context).getAuthKey();
        }else{
            return "";
        }
    }

    @AuthKeyQualifier
    @ApplicationScope
    @Provides
    AbstractionRouter provideAbstractionRouter(Context context){
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context);
        }else{
            return null;
        }
    }

    @FreshAccessTokenQualifier
    @ApplicationScope
    @Provides
    String provideFreshToken(Context context){
        if(context instanceof AbstractionRouter){
            return ((AbstractionRouter)context).getFreshToken();
        }else{
            return "";
        }
    }
}
