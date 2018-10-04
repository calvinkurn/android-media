package com.tokopedia.analytics.debugger.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerFragment;

import dagger.Component;

/**
 * @author okasurya on 5/17/18.
 */

@Component(modules = AnalyticsDebuggerModule.class, dependencies = BaseAppComponent.class)
@AnalyticsDebuggerScope
public interface AnalyticsDebuggerComponent {
    void inject(AnalyticsDebuggerFragment fragment);
}
