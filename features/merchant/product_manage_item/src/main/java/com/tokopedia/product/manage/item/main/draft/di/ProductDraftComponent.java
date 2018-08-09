package com.tokopedia.product.manage.item.main.draft.di;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.add.di.ProductAddComponent;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment;

import dagger.Component;

/**
 * @author sebastianuskh on 4/26/17.
 */

@ProductAddScope
@Component(modules = ProductDraftModule.class, dependencies = ProductComponent.class)
public interface ProductDraftComponent extends ProductAddComponent {
    void inject(ProductDraftEditFragment fragment);
}
