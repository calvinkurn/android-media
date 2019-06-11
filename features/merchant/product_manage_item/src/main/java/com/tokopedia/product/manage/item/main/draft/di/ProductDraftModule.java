package com.tokopedia.product.manage.item.main.draft.di;

import com.tokopedia.product.manage.item.main.add.di.ProductAddModule;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.draft.domain.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.SaveDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.view.presenter.ProductDraftPresenterImpl;
import com.tokopedia.product.manage.item.variant.domain.FetchProductVariantByCatUseCase;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.user.session.UserSessionInterface;

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
                                                        UserSessionInterface userSession,
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
