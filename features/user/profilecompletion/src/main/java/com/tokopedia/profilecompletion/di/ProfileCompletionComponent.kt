package com.tokopedia.profilecompletion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.settingprofile.view.fragment.SettingProfileFragment
import dagger.Component


@ProfileCompletionScope
@Component(modules = [
    ProfileCompletionModule::class,
    ProfileCompletionViewModelModule::class,
    ProfileCompletionQueryModule::class,
    ImageUploadModule::class
], dependencies = [BaseAppComponent::class])
interface ProfileCompletionComponent {
    fun inject(fragment: ChangeGenderFragment)
    fun inject(fragment: AddEmailFragment)
    fun inject(fragment: AddPhoneFragment)
    fun inject(fragment: SettingProfileFragment)

}