package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.posapp.common.ApiModule;
import com.tokopedia.posapp.product.management.data.source.ProductManagementApi;
import com.tokopedia.posapp.product.management.di.scope.ProductManagementScope;
import com.tokopedia.posapp.product.management.domain.GetProductListManagementUseCase;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.presenter.ProductManagementPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author okasurya on 3/14/18.
 */

@ProductManagementScope
@Module(includes = ApiModule.class)
public class ProductManagementModule {
    @Provides
    @ProductManagementScope
    ProductManagementApi provideProductManagementApi(Retrofit retrofit) {
        return retrofit.create(ProductManagementApi.class);
    }

    @Provides
    @ProductManagementScope
    ProductManagement.Presenter providePresenter(GetProductListManagementUseCase usecase,
                                                 UserSession userSession) {
        return new ProductManagementPresenter(usecase, userSession);
    }
}
