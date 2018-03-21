package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.posapp.di.qualifier.CloudSource;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementCloudRepository;
import com.tokopedia.posapp.product.management.data.repository.ProductManagementRepository;
import com.tokopedia.posapp.product.management.data.source.ProductManagementCloudSource;
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

    @Provides
    @CloudSource
    ProductManagementRepository provideCloudRepository(ProductManagementCloudSource cloudSource) {
        return new ProductManagementCloudRepository(cloudSource);
    }
}
