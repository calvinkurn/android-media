package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.di.module.ProductScoringModule;
import com.tokopedia.product.manage.item.view.fragment.ProductScoringDetailFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

@ActivityScope
@Component (modules = ProductScoringModule.class, dependencies = ProductComponent.class)
public interface ProductScoringComponent {
    void inject(ProductScoringDetailFragment productScoringDetailFragment);
}
