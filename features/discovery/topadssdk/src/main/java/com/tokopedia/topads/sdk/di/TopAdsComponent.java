package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsImageView;

import dagger.Component;

@TopAdsScope
@Component(modules = {TopAdsModule.class, TopAdsWishlistModule.class, TopAdsViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    void inject(BannerAdsPresenter bannerAdsPresenter);

    void inject(TopAdsBannerView topAdsBannerView);

    void inject(TopAdsImageView topAdsImageView);
}
