package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;

import dagger.Component;

@TopAdsScope
@Component(modules = TopAdsModule.class, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {
    void inject(TopAdsPresenter topAdsPresenter);

//    void inject(TopAdsWidgetView topAdsWidgetView);

//    void inject(TopAdsBannerView topAdsBannerView);
}
