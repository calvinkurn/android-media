package com.tokopedia.product.edit.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.product.edit.common.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.product.edit.common.domain.ProductDraftRepository;
import com.tokopedia.product.edit.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.edit.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.edit.di.scope.ProductAddScope;
import com.tokopedia.product.edit.view.presenter.ProductDraftPresenterImpl;
import com.tokopedia.product.edit.view.presenter.ProductEditPresenterImpl;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/26/17.
 */
@ProductAddScope
@Module(includes = ProductAddModule.class)
public class ProductDraftModule {

    @ProductAddScope
    @Provides
    ProductDraftPresenterImpl productDraftPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase,
                                                        GetShopInfoUseCase getShopInfoUseCase,
                                                        UserSession userSession,
                                                        FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase,
                                                        FetchDraftProductUseCase fetchDraftProductUseCase){
        return new ProductDraftPresenterImpl(saveDraftProductUseCase, getShopInfoUseCase, userSession, fetchProductVariantByCatUseCase, fetchDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        return new FetchDraftProductUseCase(productDraftRepository);
    }

}
