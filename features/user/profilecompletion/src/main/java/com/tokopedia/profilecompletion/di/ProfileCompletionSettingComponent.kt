package com.tokopedia.profilecompletion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.profilecompletion.addbod.view.fragment.AddBodFragment
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.addphone.view.fragment.NewAddPhoneFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.AddPinFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.addpin.view.fragment.PinOnboardingFragment
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.changename.view.ChangeNameFragment
import com.tokopedia.profilecompletion.changepin.view.fragment.ChangePinFragment
import com.tokopedia.profilecompletion.profilecompletion.view.fragment.ProfileCompletionDateFragment
import com.tokopedia.profilecompletion.profilecompletion.view.fragment.ProfileCompletionFragment
import com.tokopedia.profilecompletion.profilecompletion.view.fragment.ProfileCompletionGenderFragment
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewFragment
import com.tokopedia.profilecompletion.profileinfo.view.bottomsheet.CloseAccountBottomSheet
import com.tokopedia.profilecompletion.profileinfo.view.fragment.ProfileInfoFragment
import dagger.Component


@ProfileCompletionSettingScope
@Component(
    modules = [
	ProfileCompletionSettingModule::class,
	ProfileCompletionViewModelsModule::class,
	ProfileCompletionQueryModule::class,
	ImageUploadSettingProfileModule::class,
	MediaUploaderModule::class
    ], dependencies = [BaseAppComponent::class]
)
interface ProfileCompletionSettingComponent {
    fun inject(fragment: ChangeGenderFragment)
    fun inject(fragment: AddEmailFragment)
    fun inject(fragment: AddPhoneFragment)
    fun inject(fragment: NewAddPhoneFragment)
    fun inject(fragment: AddBodFragment)
    fun inject(fragment: AddPinFragment)
    fun inject(fragment: PinOnboardingFragment)
    fun inject(fragment: PinCompleteFragment)
    fun inject(fragment: ChangePinFragment)
    fun inject(fragment: ChangeNameFragment)
    fun inject(fragment: ProfileCompletionFragment)
    fun inject(fragment: ProfileCompletionGenderFragment)
    fun inject(fragment: ProfileCompletionDateFragment)
    fun inject(fragment: ProfileInfoFragment)
    fun inject(fragment: ChangeBioUsernameFragment)
    fun inject (fragment: ProfileSettingWebViewFragment)
    fun inject(bottomSheet: CloseAccountBottomSheet)
}
