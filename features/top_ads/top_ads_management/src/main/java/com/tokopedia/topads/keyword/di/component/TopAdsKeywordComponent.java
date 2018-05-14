package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAdListFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsOldKeywordListFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsOldKeywordNegativeListFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordComponent {
    void inject(TopAdsOldKeywordListFragment topAdsOldKeywordListFragment);

    void inject(TopAdsOldKeywordNegativeListFragment topAdsKeywordNegativeListFragment);

    void inject(TopAdsKeywordAdListFragment topAdsKeywordAdListFragment);
}
