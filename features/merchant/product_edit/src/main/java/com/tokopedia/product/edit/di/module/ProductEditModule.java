package com.tokopedia.product.edit.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.product.edit.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.edit.data.mapper.EditProductFormMapper;
import com.tokopedia.product.edit.data.source.EditProductFormDataSource;
import com.tokopedia.product.edit.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.product.edit.data.source.ProductDataSource;
import com.tokopedia.product.edit.data.source.cloud.api.EditProductFormApi;
import com.tokopedia.product.edit.data.repository.EditProductFormRepositoryImpl;
import com.tokopedia.product.edit.data.repository.ProductRepositoryImpl;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.domain.EditProductFormRepository;
import com.tokopedia.product.edit.domain.ProductRepository;
import com.tokopedia.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.edit.view.presenter.ProductEditPresenterImpl;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/21/17.
 */

@ProductAddScope
@Module(includes = ProductAddModule.class)
public class ProductEditModule{

    @ProductAddScope
    @Provides
    ProductEditPresenterImpl provideProductEditPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                                             GetShopInfoUseCase getShopInfoUseCase,
                                                             UserSession userSession,
                                                             GetProductDetailUseCase getProductDetailUseCase){
        return new ProductEditPresenterImpl(saveDraftProductUseCase, getShopInfoUseCase, userSession, getProductDetailUseCase);
    }

    @ProductAddScope
    @Provides
    EditProductFormRepository provideEditProductFormRepository(
            EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper,
            FetchVideoEditProductDataSource fetchVideoEditProductDataSource
    ) {
        return new EditProductFormRepositoryImpl(editProductFormDataSource, editProductFormMapper, fetchVideoEditProductDataSource);
    }

    @ProductAddScope
    @Provides
    EditProductFormApi provideEditProductFormApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(EditProductFormApi.class);
    }

    @ProductAddScope
    @Provides
    GoldMerchantService provideGoldMerchantService() {
        return new GoldMerchantService();
    }

    @ProductAddScope
    @Provides
    FetchVideoEditProductDataSource provideFetchVideoEditProductDataSource(GoldMerchantService goldMerchantService) {
        return new FetchVideoEditProductDataSource(goldMerchantService);
    }

    @ProductAddScope
    @Provides
    ProductRepository provideProductRepository(ProductDataSource productDataSource,
                                               FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        return new ProductRepositoryImpl(productDataSource, fetchVideoEditProductDataSource);
    }

    @ProductAddScope
    @Provides
    ProductUploadMapper provideProductUploadMapper() {
        return new ProductUploadMapper();
    }
}