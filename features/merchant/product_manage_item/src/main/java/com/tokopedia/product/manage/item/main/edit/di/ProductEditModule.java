package com.tokopedia.product.manage.item.main.edit.di;

import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.product.manage.item.common.domain.GetProductDetailUseCase;
import com.tokopedia.product.manage.item.main.add.di.ProductAddModule;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.base.data.mapper.EditProductFormMapper;
import com.tokopedia.product.manage.item.main.base.data.repository.EditProductFormRepositoryImpl;
import com.tokopedia.product.manage.item.main.base.data.repository.ProductRepositoryImpl;
import com.tokopedia.product.manage.item.main.base.data.source.EditProductFormDataSource;
import com.tokopedia.product.manage.item.main.base.data.source.ProductDataSource;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.EditProductFormApi;
import com.tokopedia.product.manage.item.main.base.domain.EditProductFormRepository;
import com.tokopedia.product.manage.item.main.base.domain.ProductRepository;
import com.tokopedia.product.manage.item.main.base.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.main.edit.view.presenter.ProductEditPresenterImpl;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.product.manage.item.video.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
                                                             UserSessionInterface userSession,
                                                             FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase,
                                                             GetProductDetailUseCase getProductDetailUseCase){
        return new ProductEditPresenterImpl(saveDraftProductUseCase, getShopInfoUseCase, userSession, fetchProductVariantByCatUseCase, getProductDetailUseCase);
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