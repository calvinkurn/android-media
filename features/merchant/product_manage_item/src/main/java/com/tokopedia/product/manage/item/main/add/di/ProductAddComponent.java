package com.tokopedia.product.manage.item.main.add.di;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.add.view.fragment.ProductAddFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Component(modules = ProductAddModule.class, dependencies = ProductComponent.class)
public interface ProductAddComponent {
//    void inject(ProductAddNameCategoryFragment productAddFragment);
    void inject(ProductAddFragment productAddFragment);
}
