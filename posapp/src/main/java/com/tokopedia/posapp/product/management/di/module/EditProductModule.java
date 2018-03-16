package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.posapp.product.management.di.scope.EditProductScope;
import com.tokopedia.posapp.product.management.view.EditProduct;
import com.tokopedia.posapp.product.management.view.presenter.EditProductPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 3/14/18.
 */
@EditProductScope
@Module
public class EditProductModule {
    @EditProductScope
    @Provides
    EditProduct.Presenter providesEditProduct() {
        return new EditProductPresenter();
    }
}
