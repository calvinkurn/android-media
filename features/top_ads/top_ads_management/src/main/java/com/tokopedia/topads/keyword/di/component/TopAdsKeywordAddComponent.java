package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordAddModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAddFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewAddFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordAddModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordAddComponent {
    void inject(TopAdsKeywordAddFragment topAdsKeywordAddFragment);
    void inject(TopAdsKeywordNewAddFragment topAdsKeywordAddFragment);
}
