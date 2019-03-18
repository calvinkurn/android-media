package com.tokopedia.product.manage.item.main.add.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.common.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.core.common.category.data.source.CategoryDataSource;
import com.tokopedia.core.common.category.data.source.CategoryVersionDataSource;
import com.tokopedia.core.common.category.data.source.FetchCategoryDataSource;
import com.tokopedia.core.common.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.core.common.category.domain.CategoryRepository;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.MerlinQualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.product.manage.item.catalog.data.repository.CatalogRepositoryImpl;
import com.tokopedia.product.manage.item.catalog.data.source.CatalogDataSource;
import com.tokopedia.product.manage.item.catalog.domain.CatalogRepository;
import com.tokopedia.product.manage.item.category.data.repository.CategoryRecommRepositoryImpl;
import com.tokopedia.product.manage.item.category.data.source.CategoryRecommDataSource;
import com.tokopedia.product.manage.item.category.domain.CategoryRecommRepository;
import com.tokopedia.product.manage.item.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.product.manage.item.main.add.view.listener.ProductAddView;
import com.tokopedia.product.manage.item.main.add.view.presenter.ProductAddPresenterImpl;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.MerlinApi;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.SearchApi;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.variant.data.repository.ProductVariantRepository;
import com.tokopedia.product.manage.item.variant.data.repository.ProductVariantRepositoryImpl;
import com.tokopedia.product.manage.item.variant.data.source.ProductVariantDataSource;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module(includes = ShopCommonModule.class)
public class ProductAddModule {

    @ProductAddScope
    @Provides
    ProductAddPresenterImpl<ProductAddView> provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                                                       GetShopInfoUseCase getShopInfoUseCase, UserSessionInterface userSession,
                                                                       FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase){
        return new ProductAddPresenterImpl<>(saveDraftProductUseCase, getShopInfoUseCase, userSession, fetchProductVariantByCatUseCase);
    }

    @ProductAddScope
    @Provides
    AbstractionRouter abstractionRouter(@ApplicationContext Context context){
        if(context instanceof AbstractionRouter){
            return (AbstractionRouter) context;
        }
        return null;
    }

    @ProductAddScope
    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
    @Provides
    Context context(@ApplicationContext Context context){
        return context;
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
