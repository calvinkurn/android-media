package com.tokopedia.feedplus.view.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.feedplus.view.fragment.FeedPlusDetailFragment;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.feedplus.view.fragment.RecentViewFragment;

import dagger.Component;

/**
 * @author by nisie on 5/15/17.
 */

@FeedPlusScope
@Component(modules = FeedPlusModule.class, dependencies = BaseAppComponent.class)
public interface FeedPlusComponent {

    void inject(FeedPlusFragment feedPlusFragment);

    void inject(FeedPlusDetailFragment feedPlusDetailFragment);

    void inject(RecentViewFragment recentViewFragment);

    FollowKolPostUseCase getFollowKolPostUseCase();
}
