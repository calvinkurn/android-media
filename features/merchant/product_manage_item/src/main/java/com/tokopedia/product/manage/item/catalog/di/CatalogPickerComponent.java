package com.tokopedia.product.manage.item.catalog.di;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.manage.item.catalog.view.presenter.CatalogPickerPresenter;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;

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
