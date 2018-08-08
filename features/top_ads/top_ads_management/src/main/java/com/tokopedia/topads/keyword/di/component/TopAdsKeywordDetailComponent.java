package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordDetailModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordDetailFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordDetailModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordDetailComponent {
    void inject(TopAdsKeywordDetailFragment topAdsKeywordDetailFragment);
}
