package com.tokopedia.profile.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profile.view.fragment.ProfileFragment
import dagger.Component

/**
 * @author by milhamj on 9/21/18.
 */
@ProfileScope
@Component(
        modules = arrayOf(ProfileModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)
}