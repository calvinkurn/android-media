package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.di.module.AddProductserviceModule;
import com.tokopedia.product.manage.item.di.scope.AddProductServiceScope;
import com.tokopedia.product.manage.item.view.service.UploadProductService;

import dagger.Component;

/**
 * @author sebastianuskh on 4/20/17.
 */
@AddProductServiceScope
@Component(modules = AddProductserviceModule.class, dependencies = ProductComponent.class)
public interface AddProductServiceComponent {
    void inject(UploadProductService uploadProductService);
}
