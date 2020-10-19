package com.tokopedia.tkpd.thankyou.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.thankyou.di.module.ThanksTrackerModule;
import com.tokopedia.tkpd.thankyou.di.scope.ThanksTrackerScope;
import com.tokopedia.tkpd.thankyou.view.ThanksTrackerService;

import dagger.Component;

/**
 * Created by okasurya on 12/4/17.
 */

@ThanksTrackerScope
@Component(modules = ThanksTrackerModule.class, dependencies = AppComponent.class)
public interface ThanksTrackerComponent {
    void inject(ThanksTrackerService service);
}
