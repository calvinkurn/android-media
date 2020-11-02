package com.tokopedia.navigation.presentation.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;

import dagger.Component;

/**
 * Created by meta on 25/07/18.
 */
@Component(modules = TestGlobalNavModule.class, dependencies = BaseAppComponent.class)
@GlobalNavScope
public interface TestGlobalNavComponent extends GlobalNavComponent {

    GetDrawerNotificationUseCase getGetDrawerNotificationUseCase();

    MainParentPresenter mainParentPresenter();
}
