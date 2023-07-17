package com.tokopedia.people.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment
import com.tokopedia.people.views.fragment.FollowerListingFragment
import com.tokopedia.people.views.fragment.FollowingListingFragment
import com.tokopedia.people.views.fragment.UserProfileFeedFragment
import com.tokopedia.people.views.fragment.UserProfileFragment
import com.tokopedia.people.views.fragment.UserProfileReviewFragment
import com.tokopedia.people.views.fragment.UserProfileVideoFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
@Module
abstract class UserProfileFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(UserProfileFragment::class)
    abstract fun bindUserProfileFragment(fragment: UserProfileFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(UserProfileFeedFragment::class)
    abstract fun bindUserProfileFeedFragment(fragment: UserProfileFeedFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(UserProfileVideoFragment::class)
    abstract fun bindUserProfileVideoFragment(fragment: UserProfileVideoFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(UserProfileReviewFragment::class)
    abstract fun bindUserProfileReviewFragment(fragment: UserProfileReviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FollowerFollowingListingFragment::class)
    abstract fun bindFollowerFollowingListingFragment(fragment: FollowerFollowingListingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FollowerListingFragment::class)
    abstract fun bindFollowerListingFragment(fragment: FollowerListingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FollowingListingFragment::class)
    abstract fun bindFollowingListingFragment(fragment: FollowingListingFragment): Fragment
}
