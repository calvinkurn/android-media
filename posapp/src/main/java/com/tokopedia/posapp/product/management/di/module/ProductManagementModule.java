package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.posapp.product.management.di.scope.ProductManagementScope;
import com.tokopedia.posapp.product.management.view.ProductManagement;
import com.tokopedia.posapp.product.management.view.presenter.ProductManagementPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 3/14/18.
 */

@ProductManagementScope
@Module
public class ProductManagementModule {
    @Provides
    @ProductManagementScope
    ProductManagement.Presenter providePresenter() {
        return new ProductManagementPresenter();
    }
}
