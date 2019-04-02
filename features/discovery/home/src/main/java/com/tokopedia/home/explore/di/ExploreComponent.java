package com.tokopedia.home.explore.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.home.common.ApiModule;
import com.tokopedia.home.explore.view.activity.ExploreActivity;
import com.tokopedia.home.explore.view.fragment.ExploreFragment;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;

import dagger.Component;

/**
 * Created by errysuprayogi on 2/2/18.
 */
@ExploreScope
@Component(modules = {ApiModule.class, ExploreModule.class}, dependencies = BaseAppComponent.class)
public interface ExploreComponent {
    void inject(ExploreActivity exploreActivity);

    void inject(ExplorePresenter explorePresenter);

    void inject(ExploreFragment exploreFragment);
}
