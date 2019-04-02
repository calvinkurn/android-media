package com.tokopedia.posapp.react.di.module;

import com.tokopedia.posapp.cache.di.PosCacheModule;
import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.product.common.di.ProductModule;
import com.tokopedia.posapp.react.di.scope.ReactDataScope;

import dagger.Module;

/**
 * Created by okasurya on 9/16/17.
 */

@ReactDataScope
@Module(includes = {ProductModule.class, PosCacheModule.class, CartModule.class})
public class ReactDataModule {
}
