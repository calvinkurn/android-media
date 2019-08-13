package com.tokopedia.purchase_platform.common.di.module;

import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 16/05/18.
 */
@Module
public class DataUtilModule {

    @Provides
    CartApiRequestParamGenerator cartApiRequestParamGenerator() {
        return new CartApiRequestParamGenerator();
    }
}
