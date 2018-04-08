package com.tokopedia.posapp.product.management.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.common.PosApiModule;
import com.tokopedia.posapp.etalase.domain.GetEtalaseCacheUseCase;
import com.tokopedia.posapp.product.common.di.ProductModule;
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
@Module(includes = {PosApiModule.class})
public class ProductManagementModule {
    @Provides
    @ProductManagementScope
    ProductManagementApi provideProductManagementApi(Retrofit retrofit) {
        return retrofit.create(ProductManagementApi.class);
    }

    @Provides
    @ProductManagementScope
    PosSessionHandler providePosSessionHandler(@ApplicationContext Context context) {
        return new PosSessionHandler(context);
    }

    @Provides
    @ProductManagementScope
    ProductManagement.Presenter providePresenter(GetProductListManagementUseCase getProductListManagementUseCase,
                                                 UserSession userSession,
                                                 PosSessionHandler posSessionHandler) {
        return new ProductManagementPresenter(getProductListManagementUseCase, userSession, posSessionHandler);
    }
}
