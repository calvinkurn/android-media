package com.tokopedia.posapp.product.management.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.posapp.product.management.di.module.EditProductModule;
import com.tokopedia.posapp.product.management.di.scope.EditProductScope;
import com.tokopedia.posapp.product.management.view.fragment.EditProductDialogFragment;

import dagger.Component;

/**
 * @author okasurya on 3/14/18.
 */

@EditProductScope
@Component(modules = EditProductModule.class, dependencies = BaseAppComponent.class)
public interface EditProductComponent {
    void inject(EditProductDialogFragment fragment);
}
