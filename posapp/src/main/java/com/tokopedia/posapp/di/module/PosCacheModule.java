package com.tokopedia.posapp.di.module;

import com.tokopedia.posapp.di.scope.PosCacheScope;
import com.tokopedia.posapp.di.scope.ShopScope;

import dagger.Module;

/**
 * Created by okasurya on 9/5/17.
 */
// TODO: 9/20/17 fix scope structure
@PosCacheScope
@ShopScope
@Module(includes = {EtalaseModule.class, ShopProductModule.class, BankModule.class})
public class PosCacheModule {

}
