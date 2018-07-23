package com.tokopedia.topads.group.di.component;

import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.group.di.module.TopAdsGroupAdListModule;
import com.tokopedia.topads.group.di.scope.TopAdsGroupAdScope;
import com.tokopedia.topads.group.view.fragment.TopAdsGroupAdListFragment;

import dagger.Component;

/**
 * Created by hadi.putra on 09/05/18.
 */
@TopAdsGroupAdScope
@Component(modules = {TopAdsGroupAdListModule.class}, dependencies = {TopAdsComponent.class})
public interface TopAdsGroupAdListComponent {
    void inject(TopAdsGroupAdListFragment fragment);
}
