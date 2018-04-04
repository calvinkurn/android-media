package com.tokopedia.gm.common.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.gm.cashback.data.repository.GmCashbackRepositoryImpl;
import com.tokopedia.gm.cashback.data.source.CashbackApi;
import com.tokopedia.gm.cashback.data.source.GMCashbackDataSource;
import com.tokopedia.gm.cashback.domain.GMCashbackRepository;
import com.tokopedia.gm.featured.data.GMFeaturedProductDataSource;
import com.tokopedia.gm.featured.data.cloud.api.GMFeaturedProductApi;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductMapper;
import com.tokopedia.gm.featured.domain.mapper.GMFeaturedProductSubmitMapper;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepository;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepositoryImpl;
import com.tokopedia.seller.base.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.seller.base.data.source.DatePickerDataSource;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenterImpl;
import com.tokopedia.gm.common.di.scope.GMScope;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.shop.common.data.source.ShopInfoDataSource;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GMScope
@Module
public class GMModule {

    @GMScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @GMScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @GMScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @GMScope
    @Provides
    GMFeaturedProductApi provideFeaturedProductApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMFeaturedProductApi.class);
    }

    @GMScope
    @Provides
    GMFeaturedProductRepository provideFeaturedProductRepository(
            GMFeaturedProductDataSource gmFeaturedProductDataSource,
            ShopInfoRepository shopInfoRepository,
            GMFeaturedProductMapper gmFeaturedProductMapper,
            GMFeaturedProductSubmitMapper gmFeaturedProductSubmitMapper) {
        return new GMFeaturedProductRepositoryImpl(gmFeaturedProductDataSource, shopInfoRepository, gmFeaturedProductMapper, gmFeaturedProductSubmitMapper);
    }

    @GMScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMScope
    @Provides
    TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeApi.class);
    }
    @Provides
    @GMScope
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @Provides
    @GMScope
    public GMCashbackRepository provideCashbackRepository(GMCashbackDataSource gmCashbackDataSource){
        return new GmCashbackRepositoryImpl(gmCashbackDataSource);
    }

    @Provides
    @GMScope
    public CashbackApi cashbackApi(@GoldMerchantQualifier Retrofit retrofit){
        return retrofit.create(CashbackApi.class);
    }

}