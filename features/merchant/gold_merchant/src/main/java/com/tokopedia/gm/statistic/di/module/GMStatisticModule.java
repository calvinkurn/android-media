package com.tokopedia.gm.statistic.di.module;

import android.content.Context;
import android.content.res.AssetManager;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.gm.statistic.data.source.GMStatDataSource;
import com.tokopedia.gm.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.gm.statistic.di.scope.GMStatisticScope;
import com.tokopedia.gm.statistic.domain.GMStatRepository;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatGetTransactionTableUseCase;
import com.tokopedia.gm.statistic.domain.interactor.GMStatMarketInsightUseCase;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.gm.statistic.view.presenter.GMDashboardPresenter;
import com.tokopedia.gm.statistic.view.presenter.GMDashboardPresenterImpl;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionPresenterImpl;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.gm.statistic.view.presenter.GMStatisticTransactionTablePresenterImpl;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.shop.common.data.source.ShopInfoDataSource;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author normansyahputa on 7/6/17.
 */
@GMStatisticScope
@Module
public class GMStatisticModule {

    @GMStatisticScope
    @Provides
    GMStatApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }

    @GMStatisticScope
    @Provides
    GMStatRepository provideGMStatRepository(GMStatDataSource gmStatDataSource,
                                             GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                             GMTransactionTableMapper gmTransactionTableMapper,
                                             ShopInfoRepository shopInfoRepository) {
        return new GMStatRepositoryImpl(gmTransactionStatDomainMapper, gmStatDataSource, gmTransactionTableMapper,
                shopInfoRepository);
    }

    @GMStatisticScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMStatisticScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @GMStatisticScope
    @Provides
    TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeApi.class);
    }

    @GMStatisticScope
    @Provides
    GMStatisticTransactionTablePresenter provideGmStatisticTransactionTablePresenter(
            GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase
    ) {
        return new GMStatisticTransactionTablePresenterImpl(gmStatGetTransactionTableUseCase);
    }

    @GMStatisticScope
    @Provides
    GMStatisticTransactionPresenter provideGmStatisticTransactionPresenter(
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            SessionHandler sessionHandler) {
        return new GMStatisticTransactionPresenterImpl(gmStatGetTransactionGraphUseCase, sessionHandler);
    }

    @GMStatisticScope
    @Provides
    public GMDashboardPresenter provideGmFragmentPresenter(GMStatMarketInsightUseCase marketInsightUseCase,
                                                           GMStatGetBuyerGraphUseCase buyerGraphUseCase,
                                                           GMStatGetPopularProductUseCase popularProductUseCase,
                                                           GMStatGetTransactionGraphUseCase getTransactionGraphUseCase,
                                                           GMStatGetProductGraphUseCase productGraphUseCase,
                                                           AddProductShopInfoUseCase shopInfoUseCase) {
        return new GMDashboardPresenterImpl(marketInsightUseCase, buyerGraphUseCase, popularProductUseCase,
                getTransactionGraphUseCase, productGraphUseCase, shopInfoUseCase);
    }

    @GMStatisticScope
    @Provides
    public AssetManager provideAssetManager(@ApplicationContext Context context) {
        return context.getAssets();
    }

    @GMStatisticScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource, CategoryDataSource categoryDataSource, FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @GMStatisticScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }

    @GMStatisticScope
    @Provides
    public GMModuleRouter provideGmModuleRouter(@ApplicationContext Context context){
        return (GMModuleRouter)context;
    }
}
