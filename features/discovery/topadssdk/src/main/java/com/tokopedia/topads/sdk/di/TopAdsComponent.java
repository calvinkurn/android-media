package com.tokopedia.topads.sdk.di;

import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;

import dagger.Component;

@TopAdsScope
@Component(modules = TopAdsModule.class)
public interface TopAdsComponent {
    void inject(TopAdsPresenter topAdsPresenter);
}
