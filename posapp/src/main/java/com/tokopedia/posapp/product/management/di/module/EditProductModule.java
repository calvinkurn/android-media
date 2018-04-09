package com.tokopedia.posapp.product.management.di.module;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.product.management.di.scope.EditProductScope;
import com.tokopedia.posapp.product.management.domain.EditProductLocalPriceUseCase;
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
    EditProduct.Presenter providesEditProduct(EditProductLocalPriceUseCase editProductLocalPriceUseCase,
                                              PosSessionHandler posSessionHandler,
                                              UserSession usersession) {
        return new EditProductPresenter(editProductLocalPriceUseCase, posSessionHandler, usersession);
    }
}
