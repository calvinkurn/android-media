package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.view.adapter.TopAdsPlacer;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;
import com.tokopedia.topads.sdk.widget.TopAdsDynamicChannelView;
import com.tokopedia.topads.sdk.widget.TopAdsView;
import com.tokopedia.topads.sdk.widget.TopAdsWidgetView;

import dagger.Component;

@TopAdsScope
@Component(modules = TopAdsModule.class, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    void inject(BannerAdsPresenter bannerAdsPresenter);

    void inject(TopAdsPresenter topAdsPresenter);

    void inject(TopAdsWidgetView topAdsWidgetView);

    void inject(TopAdsBannerView topAdsBannerView);

    void inject(TopAdsView topAdsView);

    void inject(TopAdsCarouselView topAdsCarouselView);

    void inject(TopAdsPlacer topAdsPlacer);

    void inject(TopAdsDynamicChannelView topAdsDynamicChannelView);
}
