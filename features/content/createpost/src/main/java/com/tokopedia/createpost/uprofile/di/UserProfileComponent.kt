package com.tokopedia.createpost.uprofile.di

import com.tokopedia.createpost.uprofile.views.FollowerFollowingListingFragment
import com.tokopedia.createpost.uprofile.views.FollowerListingFragment
import com.tokopedia.createpost.uprofile.views.FollowingListingFragment
import com.tokopedia.createpost.uprofile.views.UserProfileFragment
import dagger.Component

@UserProfileScope
@Component(modules = [UserProfileModule::class, UserProfileViewModelModule::class])
interface UserProfileComponent {
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: FollowerFollowingListingFragment)
    fun inject(fragment: FollowingListingFragment)
    fun inject(fragment: FollowerListingFragment)
}
