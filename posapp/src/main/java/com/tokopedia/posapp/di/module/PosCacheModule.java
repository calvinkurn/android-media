package com.tokopedia.posapp.di.module;

import com.tokopedia.posapp.di.scope.PosCacheScope;

import dagger.Module;

/**
 * Created by okasurya on 9/5/17.
 */
// TODO: 9/20/17 fix scope structure
@PosCacheScope
@Module(includes = {EtalaseModule.class, ProductModule.class, BankModule.class})
public class PosCacheModule {

}
