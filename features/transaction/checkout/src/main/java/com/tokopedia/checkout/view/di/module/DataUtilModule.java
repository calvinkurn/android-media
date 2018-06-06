package com.tokopedia.checkout.view.di.module;

import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;

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
