package com.tokopedia.analytics.mapper.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.analytics.mapper.TkpdAnalyticMapper;

import dagger.Component;

@TkpdAnalyticsMapScope
@Component(dependencies = BaseAppComponent.class)
public interface TkpdAnalyticsMapComponent {
    void inject(TkpdAnalyticMapper tkpdAnalyticMapper);
}
