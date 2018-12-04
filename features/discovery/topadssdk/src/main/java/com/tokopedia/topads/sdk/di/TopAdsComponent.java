package com.tokopedia.topads.sdk.di;

import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.widget.TopAdsView;

import dagger.Component;

@TopAdsScope
@Component(modules = TopAdsModule.class)
public interface TopAdsComponent {
    void inject(TopAdsPresenter topAdsPresenter);

    void inject(TopAdsView topAdsView);
}
