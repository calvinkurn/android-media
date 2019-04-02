package com.tokopedia.topads.keyword.di.component;

import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordGroupsFragment;
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 5/24/17.
 */

@TopAdsKeywordScope
@Component(modules = TopAdsKeywordNewChooseGroupModule.class, dependencies = TopAdsComponent.class)
public interface TopAdsKeywordNewChooseGroupComponent {

    void inject(TopAdsKeywordGroupsFragment topAdsKeywordGroupsFragment);

    void inject(TopAdsKeywordNewChooseGroupFragment topAdsKeywordNewChooseGroupFragment);
}
