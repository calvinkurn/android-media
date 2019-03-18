package com.tokopedia.tkpd.deeplink.di.module;

import com.tokopedia.tkpd.deeplink.di.scope.DeeplinkScope;

import dagger.Module;

/**
 * Created by okasurya on 1/4/18.
 */

@Module(includes = {GetShopInfoModule.class, GetProductModule.class})
@DeeplinkScope
public class DeeplinkModule {


}
