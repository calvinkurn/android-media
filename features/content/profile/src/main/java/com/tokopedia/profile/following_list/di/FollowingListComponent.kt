package com.tokopedia.profile.following_list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profile.following_list.view.fragment.UserFollowingListFragment
import com.tokopedia.profile.following_list.view.fragment.ShopFollowingListFragment
import com.tokopedia.kolcommon.di.KolCommonModule

import dagger.Component

/**
 * @author by milhamj on 24/04/18.
 */

@FollowingListScope
@Component(
        modules = [FollowingListModule::class, KolCommonModule::class],
        dependencies = [BaseAppComponent::class]
)
interface FollowingListComponent {

    fun inject(userFollowingListFragment: UserFollowingListFragment)

    fun inject(kolFollowingListFragment: ShopFollowingListFragment)
}
