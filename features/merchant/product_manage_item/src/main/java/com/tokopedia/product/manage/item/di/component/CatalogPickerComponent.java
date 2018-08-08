package com.tokopedia.product.manage.item.di.component;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.di.module.CatalogPickerModule;
import com.tokopedia.product.manage.item.view.presenter.CatalogPickerPresenter;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@ActivityScope
@Component (modules = CatalogPickerModule.class, dependencies = ProductComponent.class)
public interface CatalogPickerComponent {

    CatalogPickerPresenter catalogPickerPresenter();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();
}
