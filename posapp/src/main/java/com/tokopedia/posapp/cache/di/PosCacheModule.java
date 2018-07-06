package com.tokopedia.posapp.cache.di;

import com.tokopedia.posapp.bank.di.BankModule;

import dagger.Module;

/**
 * Created by okasurya on 9/5/17.
 */

@PosCacheScope
@Module(includes = {BankModule.class})
public class PosCacheModule {

}
