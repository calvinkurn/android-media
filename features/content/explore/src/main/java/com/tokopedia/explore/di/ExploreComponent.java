package com.tokopedia.explore.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.explore.view.fragment.HashtagLandingPageFragment;
import com.tokopedia.feedcomponent.di.FeedComponentModule;

import dagger.Component;

/**
 * @author by milhamj on 23/07/18.
 */

@ExploreScope
@Component(modules = {ExploreModule.class, ExploreViewModelModule.class, FeedComponentModule.class}, dependencies = BaseAppComponent.class)
public interface ExploreComponent {
    @ApplicationContext
    Context context();

    void inject(ContentExploreFragment contentExploreFragment);
    void inject(HashtagLandingPageFragment hashtagLandingPageFragment);
}
