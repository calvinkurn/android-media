package com.tokopedia.profile.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profile.view.fragment.ProfileEmptyFragment
import com.tokopedia.profile.view.fragment.ProfileFragment
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets
import com.tokopedia.kolcommon.di.KolCommonModule
import dagger.Component

/**
 * @author by milhamj on 9/21/18.
 */
@ProfileScope
@Component(
        modules = [ProfileModule::class, KolCommonModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ProfileComponent {
    fun inject(fragment: ProfileFragment)

    fun inject(fragment: ProfileEmptyFragment)

    fun inject(bottomSheets: ShareBottomSheets)
}