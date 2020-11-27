package com.tokopedia.tkpd.deeplink.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.deeplink.di.module.DeeplinkModule;
import com.tokopedia.tkpd.deeplink.di.scope.DeeplinkScope;
import com.tokopedia.tkpd.deeplink.presenter.DeepLinkPresenterImpl;

import dagger.Component;

/**
 * Created by okasurya on 1/4/18.
 */
@DeeplinkScope
@Component(modules = DeeplinkModule.class, dependencies = BaseAppComponent.class)
public interface DeeplinkComponent {
    void inject(DeepLinkPresenterImpl deepLinkPresenter);
}
