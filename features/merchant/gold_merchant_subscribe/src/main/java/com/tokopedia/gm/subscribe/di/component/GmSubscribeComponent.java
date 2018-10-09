package com.tokopedia.gm.subscribe.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.gm.subscribe.di.module.GmSubscribeModule;
import com.tokopedia.gm.subscribe.di.scope.GmSubscribeScope;
import com.tokopedia.gm.subscribe.view.presenter.GmCheckoutPresenterImpl;
import com.tokopedia.gm.subscribe.view.presenter.GmHomePresenterImpl;
import com.tokopedia.gm.subscribe.view.presenter.GmProductPresenterImpl;

import dagger.Component;

/**
 * @author sebastianuskh on 5/2/17.
 */
@GmSubscribeScope
@Component(modules = GmSubscribeModule.class, dependencies = AppComponent.class)
public interface GmSubscribeComponent {
    GmHomePresenterImpl getHomePresenter();

    GmProductPresenterImpl getProductPresenter();

    GmCheckoutPresenterImpl getCheckoutPresenter();
}
