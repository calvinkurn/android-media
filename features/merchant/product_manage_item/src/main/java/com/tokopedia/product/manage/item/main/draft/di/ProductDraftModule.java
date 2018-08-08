package com.tokopedia.product.manage.item.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.product.manage.item.common.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.common.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.product.manage.item.common.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;
import com.tokopedia.product.manage.item.view.presenter.ProductDraftPresenterImpl;
import com.tokopedia.product.manage.item.view.presenter.ProductEditPresenterImpl;
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
