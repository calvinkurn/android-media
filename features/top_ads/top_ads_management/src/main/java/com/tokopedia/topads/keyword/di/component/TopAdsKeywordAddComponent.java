package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordAddModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordAddFragment;

import dagger.Component;

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordAddModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordAddComponent {
    void inject(TopAdsKeywordAddFragment topAdsKeywordAddFragment);
}
