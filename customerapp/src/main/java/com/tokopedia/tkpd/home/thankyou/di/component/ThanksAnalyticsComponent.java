package com.tokopedia.tkpd.home.thankyou.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.thankyou.di.module.ThanksAnalyticsModule;
import com.tokopedia.tkpd.home.thankyou.di.scope.ThanksAnalyticsScope;
import com.tokopedia.tkpd.home.thankyou.view.ThanksAnalyticsService;

import dagger.Component;

/**
 * Created by okasurya on 12/4/17.
 */

@ThanksAnalyticsScope
@Component(modules = ThanksAnalyticsModule.class, dependencies = AppComponent.class)
public interface ThanksAnalyticsComponent {
    void inject(ThanksAnalyticsService service);
}
