package com.tokopedia.tkpd.deeplink.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.tkpd.deeplink.di.scope.DeeplinkScope;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 1/4/18.
 */

@Module(includes = {ShopCommonModule.class})
public class DeeplinkModule {
    @DeeplinkScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
