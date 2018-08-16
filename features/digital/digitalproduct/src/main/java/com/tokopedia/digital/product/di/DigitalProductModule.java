package com.tokopedia.digital.product.di;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.digital.product.data.mapper.USSDMapper;
import com.tokopedia.digital.product.data.repository.UssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.domain.interactor.ProductDigitalInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * @author by furqan on 25/06/18.
 */

@Module
public class DigitalProductModule {

    @Provides
    @DigitalProductScope
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, "DIGITAL_LAST_INPUT_CLIENT_NUMBER");
    }

    @Provides
    @DigitalProductScope
    USSDMapper provideUSSDMapper() {
        return new USSDMapper();
    }

    @Provides
    @DigitalProductScope
    IUssdCheckBalanceRepository provideUssdCheckBalanceRepository(DigitalRestApi digitalRestApi,
                                                                  USSDMapper ussdMapper) {
        return new UssdCheckBalanceRepository(digitalRestApi, ussdMapper);
    }

    @Provides
    @DigitalProductScope
    IProductDigitalInteractor provideProductDigitalInteractor(IUssdCheckBalanceRepository ussdCheckBalanceRepository) {
        return new ProductDigitalInteractor(ussdCheckBalanceRepository);
    }

}
