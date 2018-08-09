package com.tokopedia.product.manage.item.main.edit.di;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.add.di.ProductAddComponent;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.edit.view.fragment.ProductEditFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/21/17.
 */
@ProductAddScope
@Component(modules = ProductEditModule.class, dependencies = ProductComponent.class)
public interface ProductEditComponent extends ProductAddComponent {
    void inject(ProductEditFragment fragment);
}
