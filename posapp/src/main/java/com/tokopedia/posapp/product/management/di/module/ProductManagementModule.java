package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.product.management.data.source.ProductManagementApi;
import com.tokopedia.posapp.product.management.di.scope.ProductManagementScope;
import com.tokopedia.posapp.product.management.domain.EditProductUseCase;
import com.tokopedia.posapp.product.management.domain.GetProductListManagementUseCase;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.presenter.EditProductPresenter;
import com.tokopedia.posapp.product.management.view.presenter.ProductManagementPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author okasurya on 3/14/18.
 */

@ProductManagementScope
@Module
public class ProductManagementModule {
    @Provides
    @ProductManagementScope
    ProductManagementApi provideProductManagementApi(Retrofit retrofit) {
        return retrofit.create(ProductManagementApi.class);
    }

    @Provides
    @ProductManagementScope
    ProductManagement.Presenter provideProductManagementPresenter(GetProductListManagementUseCase getProductListManagementUseCase,
                                                                  EditProductUseCase editProductUseCase,
                                                                  UserSession userSession,
                                                                  PosSessionHandler posSessionHandler) {
        return new ProductManagementPresenter(getProductListManagementUseCase, editProductUseCase, userSession, posSessionHandler);
    }

    @Provides
    @ProductManagementScope
    EditProduct.Presenter providesEditProductPresenter(EditProductUseCase editProductUseCase,
                                                       PosSessionHandler posSessionHandler,
                                                       UserSession usersession) {
        return new EditProductPresenter(editProductUseCase, posSessionHandler, usersession);
    }
}
