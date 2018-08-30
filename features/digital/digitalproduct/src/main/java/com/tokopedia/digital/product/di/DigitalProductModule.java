package com.tokopedia.digital.product.di;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.digital.common.data.apiservice.DigitalGqlApiService;
import com.tokopedia.digital.common.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.common.data.repository.DigitalCategoryRepository;
import com.tokopedia.digital.common.data.source.CategoryDetailDataSource;
import com.tokopedia.digital.common.domain.IDigitalCategoryRepository;
import com.tokopedia.digital.common.domain.interactor.GetDigitalCategoryByIdUseCase;
import com.tokopedia.digital.product.data.mapper.USSDMapper;
import com.tokopedia.digital.product.data.repository.UssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.IUssdCheckBalanceRepository;
import com.tokopedia.digital.product.domain.interactor.DigitalGetHelpUrlUseCase;
import com.tokopedia.digital.product.domain.interactor.GetOperatorsByCategoryIdUseCase;
import com.tokopedia.digital.product.domain.interactor.GetProductsByOperatorIdUseCase;
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

    @Provides
    @DigitalProductScope
    DigitalGqlApiService provideDigitalGqlApiService() {
        return new DigitalGqlApiService();
    }

    @Provides
    @DigitalProductScope
    ProductDigitalMapper provideProductDigitalMapper() {
        return new ProductDigitalMapper();
    }

    @Provides
    @DigitalProductScope
    CategoryDetailDataSource provideCategoryDetailDataSource(DigitalGqlApiService digitalEndpointService,
                                                             CacheManager cacheManager,
                                                             ProductDigitalMapper productDigitalMapper) {
        return new CategoryDetailDataSource(digitalEndpointService, cacheManager, productDigitalMapper);
    }

    @Provides
    @DigitalProductScope
    IDigitalCategoryRepository provideDigitalCategoryRepository(CategoryDetailDataSource categoryDetailDataSource) {
        return new DigitalCategoryRepository(categoryDetailDataSource);
    }

    @Provides
    @DigitalProductScope
    GetDigitalCategoryByIdUseCase provideGetDigitalCategoryByIdUseCase(@ApplicationContext Context context,
                                                                       IDigitalCategoryRepository digitalCategoryRepository) {
        return new GetDigitalCategoryByIdUseCase(context, digitalCategoryRepository);
    }

    @Provides
    @DigitalProductScope
    GetOperatorsByCategoryIdUseCase provideGetOperatorsByCategoryIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        return new GetOperatorsByCategoryIdUseCase(getDigitalCategoryByIdUseCase);
    }

    @Provides
    @DigitalProductScope
    GetProductsByOperatorIdUseCase provideGetProductsByOperatorIdUseCase(GetDigitalCategoryByIdUseCase getDigitalCategoryByIdUseCase) {
        return new GetProductsByOperatorIdUseCase(getDigitalCategoryByIdUseCase);
    }

    @Provides
    @DigitalProductScope
    DigitalGetHelpUrlUseCase provideDigitalGetHelpUrlUseCase(IDigitalCategoryRepository digitalCategoryRepository) {
        return new DigitalGetHelpUrlUseCase(digitalCategoryRepository);
    }

}
