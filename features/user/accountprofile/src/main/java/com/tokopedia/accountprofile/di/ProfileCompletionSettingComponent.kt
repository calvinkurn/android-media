package com.tokopedia.accountprofile.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.accountprofile.common.webview.ProfileSettingWebViewFragment
import com.tokopedia.accountprofile.profilecompletion.view.fragment.ProfileCompletionDateFragment
import com.tokopedia.accountprofile.profilecompletion.view.fragment.ProfileCompletionFragment
import com.tokopedia.accountprofile.profilecompletion.view.fragment.ProfileCompletionGenderFragment
import com.tokopedia.accountprofile.settingprofile.addbod.view.fragment.AddBodFragment
import com.tokopedia.accountprofile.settingprofile.addemail.view.fragment.AddEmailFragment
import com.tokopedia.accountprofile.settingprofile.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.accountprofile.settingprofile.addphone.view.fragment.NewAddPhoneFragment
import com.tokopedia.accountprofile.settingprofile.addpin.view.fragment.AddPinFragment
import com.tokopedia.accountprofile.settingprofile.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.accountprofile.settingprofile.addpin.view.fragment.PinOnboardingFragment
import com.tokopedia.accountprofile.settingprofile.changebiousername.view.ChangeBioUsernameFragment
import com.tokopedia.accountprofile.settingprofile.changegender.view.ChangeGenderFragment
import com.tokopedia.accountprofile.settingprofile.changename.view.ChangeNameFragment
import com.tokopedia.accountprofile.settingprofile.changepin.view.fragment.ChangePinFragment
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.bottomsheet.CloseAccountBottomSheet
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.fragment.ProfileInfoFragment
import dagger.Component


@ProfileCompletionSettingScope
@Component(
    modules = [
        ProfileCompletionSettingModule::class,
        ProfileCompletionViewModelsModule::class,
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
    fun inject(fragment: ProfileSettingWebViewFragment)
    fun inject(bottomSheet: CloseAccountBottomSheet)
}
