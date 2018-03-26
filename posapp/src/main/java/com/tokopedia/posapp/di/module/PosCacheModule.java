package com.tokopedia.posapp.di.module;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.di.scope.PosCacheScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/5/17.
 */
// TODO: 9/20/17 fix scope structure
@PosCacheScope
@Module(includes = {EtalaseModule.class, ProductModule.class, BankModule.class})
public class PosCacheModule {

}
