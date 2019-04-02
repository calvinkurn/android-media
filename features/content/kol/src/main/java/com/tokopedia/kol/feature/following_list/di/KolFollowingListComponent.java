package com.tokopedia.kol.feature.following_list.di;

import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.following_list.view.fragment.KolFollowingListFragment;

import dagger.Component;

/**
 * @author by milhamj on 24/04/18.
 */

@KolFollowingListScope
@Component(modules = KolFollowingListModule.class, dependencies = KolComponent.class)
public interface KolFollowingListComponent {
    void inject(KolFollowingListFragment kolFollowingListFragment);
}
