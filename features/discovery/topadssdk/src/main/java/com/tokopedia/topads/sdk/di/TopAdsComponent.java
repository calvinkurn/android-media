package com.tokopedia.topads.sdk.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.topads.sdk.v2.tdnvertical.widget.TdnVerticalView;
import com.tokopedia.topads.sdk.old.widget.TopAdsHeadlineView;
import com.tokopedia.topads.sdk.v2.tdnbanner.widget.TopAdsImageView;
import com.tokopedia.topads.sdk.v2.tdnbanner.widget.TdnBannerView;

import dagger.Component;

@TopAdsScope
@Component(modules = {TopAdsModule.class, ViewModelModule.class}, dependencies = BaseAppComponent.class)
public interface TopAdsComponent {

    void inject(TdnBannerView tdnBannerView);

    void inject(TdnVerticalView tdnVerticalView);

    void inject(TopAdsImageView topAdsImageView);

    void inject(TopAdsHeadlineView topAdsHeadlineView);

}
