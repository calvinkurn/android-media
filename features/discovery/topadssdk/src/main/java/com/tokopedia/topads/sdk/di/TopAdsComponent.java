package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;

import dagger.Component;

@TopAdsScope
@Component(modules = {TopAdsModule.class, TopAdsWishlistModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    void inject(BannerAdsPresenter bannerAdsPresenter);

    void inject(TopAdsBannerView topAdsBannerView);

}
