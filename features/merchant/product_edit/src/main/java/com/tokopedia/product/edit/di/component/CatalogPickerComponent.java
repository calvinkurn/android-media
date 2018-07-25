package com.tokopedia.product.edit.di.component;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.product.edit.common.di.component.ProductComponent;
import com.tokopedia.product.edit.di.module.CatalogPickerModule;
import com.tokopedia.product.edit.view.presenter.CatalogPickerPresenter;

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
