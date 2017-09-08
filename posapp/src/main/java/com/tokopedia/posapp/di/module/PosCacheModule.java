package com.tokopedia.posapp.di.module;

import com.tokopedia.posapp.di.scope.PosCacheScope;

import dagger.Module;

/**
 * Created by okasurya on 9/5/17.
 */

@PosCacheScope
@Module(includes = {ShopProductModule.class, BankModule.class})
public class PosCacheModule {

}
