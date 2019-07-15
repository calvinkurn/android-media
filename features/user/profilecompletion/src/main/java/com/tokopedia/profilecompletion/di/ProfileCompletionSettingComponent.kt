package com.tokopedia.profilecompletion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.settingprofile.view.fragment.SettingProfileFragment
import dagger.Component


@ProfileCompletionSettingScope
@Component(modules = [
    ProfileCompletionSettingModule::class,
    ProfileCompletionViewModelModule::class,
    ProfileCompletionQueryModule::class,
    ImageUploadSettingProfileModule::class
], dependencies = [BaseAppComponent::class])
interface ProfileCompletionSettingComponent {
    fun inject(fragment: ChangeGenderFragment)
    fun inject(fragment: AddEmailFragment)
    fun inject(fragment: AddPhoneFragment)
    fun inject(fragment: SettingProfileFragment)

}