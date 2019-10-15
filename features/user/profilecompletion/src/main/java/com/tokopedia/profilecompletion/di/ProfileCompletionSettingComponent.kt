package com.tokopedia.profilecompletion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.profilecompletion.addbod.view.fragment.AddBodFragment
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.AddPinFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.PinOnboardingFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.settingprofile.view.fragment.SettingProfileFragment
import dagger.Component


@ProfileCompletionSettingScope
@Component(modules = [
    ProfileCompletionSettingModule::class,
    ProfileCompletionViewModelsModule::class,
    ProfileCompletionQueryModule::class,
    ImageUploadSettingProfileModule::class
], dependencies = [BaseAppComponent::class])
interface ProfileCompletionSettingComponent {
    fun inject(fragment: ChangeGenderFragment)
    fun inject(fragment: AddEmailFragment)
    fun inject(fragment: AddPhoneFragment)
    fun inject(fragment: AddBodFragment)
    fun inject(fragment: SettingProfileFragment)
    fun inject(fragment: AddPinFragment)
    fun inject(fragment: PinOnboardingFragment)
    fun inject(fragment: PinCompleteFragment)
}