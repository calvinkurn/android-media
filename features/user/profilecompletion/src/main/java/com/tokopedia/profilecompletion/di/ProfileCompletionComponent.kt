package com.tokopedia.profilecompletion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profilecompletion.changegender.ChangeGenderFragment
import dagger.Component


@ProfileCompletionScope
@Component(modules = [
    ProfileCompletionModule::class,
    ProfileCompletionViewModelModule::class,
    ProfileCompletionQueryModule::class
], dependencies = [BaseAppComponent::class])
interface ProfileCompletionComponent {
    fun inject(fragment: ChangeGenderFragment)
}