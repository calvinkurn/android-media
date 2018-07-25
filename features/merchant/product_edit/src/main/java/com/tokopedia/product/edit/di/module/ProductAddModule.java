package com.tokopedia.product.edit.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.common.domain.ProductDraftRepository;
import com.tokopedia.product.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.common.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.common.data.source.ProductDraftDataSource;
import com.tokopedia.product.common.data.source.ProductVariantDataSource;
import com.tokopedia.product.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.common.repository.ProductVariantRepository;
import com.tokopedia.product.common.repository.ProductVariantRepositoryImpl;
import com.tokopedia.product.edit.data.source.CatalogDataSource;
import com.tokopedia.product.edit.data.source.CategoryRecommDataSource;
import com.tokopedia.product.edit.data.source.ProductScoreDataSource;
import com.tokopedia.product.edit.data.source.cache.ProductScoreDataSourceCache;
import com.tokopedia.product.edit.data.source.cache.model.ProductScore.DataScoringProduct;
import com.tokopedia.product.edit.data.source.cache.model.ProductScore.DataScoringProductBuilder;
import com.tokopedia.product.edit.data.source.cloud.api.MerlinApi;
import com.tokopedia.product.edit.data.source.cloud.api.SearchApi;
import com.tokopedia.product.edit.data.repository.CatalogRepositoryImpl;
import com.tokopedia.product.edit.data.repository.CategoryRecommRepositoryImpl;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.product.edit.data.repository.ProductScoreRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.domain.CatalogRepository;
import com.tokopedia.product.edit.domain.CategoryRecommRepository;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.product.edit.domain.ProductScoreRepository;
import com.tokopedia.product.common.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.product.edit.view.presenter.ProductAddPresenter;
import com.tokopedia.product.edit.view.presenter.ProductAddPresenterImpl;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module
public class ProductAddModule {

    @ProductAddScope
    @Provides
    ProductAddPresenter provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                                   FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                                   GetCategoryRecommUseCase getCategoryRecommUseCase,
                                                   ProductScoringUseCase productScoringUseCase,
                                                   AddProductShopInfoUseCase addProductShopInfoUseCase,
                                                   FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase,
                                                   FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase){
        return new ProductAddPresenterImpl(saveDraftProductUseCase,
                fetchCatalogDataUseCase, getCategoryRecommUseCase, productScoringUseCase,
                addProductShopInfoUseCase, fetchCategoryDisplayUseCase, fetchProductVariantByCatUseCase);
    }

    @ProductAddScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource,
                                                 CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @ProductAddScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }

    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductAddScope
    @Provides
    ProductScoringUseCase provideProductScoringUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductScoreRepository productScoreRepository){
        return new ProductScoringUseCase(threadExecutor, postExecutionThread, productScoreRepository);
    }

    @ProductAddScope
    @Provides
    ProductScoreRepository provideProductScoreRepo(ProductScoreDataSource productScoreDataSource){
        return new ProductScoreRepositoryImpl(productScoreDataSource);
    }

    @ProductAddScope
    @Provides
    ProductScoreDataSource provideProductScoreDataSource(ProductScoreDataSourceCache productScoreDataSourceCache){
        return new ProductScoreDataSource(productScoreDataSourceCache);
    }

    @ProductAddScope
    @Provides
    ProductScoreDataSourceCache provideProductScoreDataSourceCache(DataScoringProduct dataScoringProduct){
        return new ProductScoreDataSourceCache(dataScoringProduct);
    }

    @ProductAddScope
    @Provides
    DataScoringProduct provideDataScoringProduct(@ApplicationContext Context context, Gson gson){
        return new DataScoringProductBuilder(context, gson).build();
    }

    // FOR SEARCH CATALOG
    @ProductAddScope
    @Provides
    CatalogRepository provideCatalogRepository(CatalogDataSource catalogDataSource) {
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @ProductAddScope
    @Provides
    SearchApi provideSearchApi(@AceQualifier Retrofit retrofit) {
        return retrofit.create(SearchApi.class);
    }

    // FOR CATEGORY RECOMMENDATION
    @ProductAddScope
    @Provides
    CategoryRecommRepository provideCategoryRecommRepository(CategoryRecommDataSource categoryRecommDataSource) {
        return new CategoryRecommRepositoryImpl(categoryRecommDataSource);
    }

    @ProductAddScope
    @Provides
    MerlinApi provideMerlinApi(@MerlinQualifier Retrofit retrofit) {
        return retrofit.create(MerlinApi.class);
    }

    // FOR SHOP_INFO
    @ProductAddScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideShopModelMapper(){
        return new SimpleDataResponseMapper<>();
    }

    @ProductAddScope
    @Provides
    ProductVariantRepository productVariantRepository(ProductVariantDataSource productVariantDataSource){
        return new ProductVariantRepositoryImpl(productVariantDataSource);
    }

}
