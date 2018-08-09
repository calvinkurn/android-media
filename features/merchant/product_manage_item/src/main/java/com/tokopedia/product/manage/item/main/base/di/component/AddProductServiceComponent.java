package com.tokopedia.product.manage.item.main.base.di.component;

import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.main.base.di.module.AddProductserviceModule;
import com.tokopedia.product.manage.item.main.base.di.scope.AddProductServiceScope;
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService;

import dagger.Component;

/**
 * @author sebastianuskh on 4/20/17.
 */
@AddProductServiceScope
@Component(modules = AddProductserviceModule.class, dependencies = ProductComponent.class)
public interface AddProductServiceComponent {
    void inject(UploadProductService uploadProductService);
}
