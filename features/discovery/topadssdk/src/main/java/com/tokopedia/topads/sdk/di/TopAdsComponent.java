package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.widget.TdnBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView;
import com.tokopedia.topads.sdk.widget.TopAdsImageView;

import dagger.Component;

@TopAdsScope
@Component(modules = {TopAdsModule.class, TopAdsWishlistModule.class, ViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    void inject(TdnBannerView tdnBannerView);

    void inject(TopAdsImageView topAdsImageView);

    void inject(TopAdsHeadlineView topAdsHeadlineView);

}
