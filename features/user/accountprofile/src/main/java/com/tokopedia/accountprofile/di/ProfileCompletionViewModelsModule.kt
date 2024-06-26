package com.tokopedia.accountprofile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.accountprofile.settingprofile.addbod.viewmodel.AddBodViewModel
import com.tokopedia.accountprofile.settingprofile.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.accountprofile.settingprofile.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.accountprofile.settingprofile.addphone.viewmodel.NewAddPhoneViewModel
import com.tokopedia.accountprofile.settingprofile.changebiousername.viewmodel.ChangeBioUsernameViewModel
import com.tokopedia.accountprofile.settingprofile.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.accountprofile.settingprofile.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.accountprofile.settingprofile.changepin.view.viewmodel.ChangePinViewModel
import com.tokopedia.accountprofile.profilecompletion.viewmodel.ProfileInfoViewModel
import com.tokopedia.accountprofile.profilecompletion.viewmodel.ProfileRoleViewModel
import com.tokopedia.accountprofile.settingprofile.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.accountprofile.settingprofile.profileinfo.viewmodel.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileCompletionViewModelsModule {
    @Binds
    @ProfileCompletionSettingScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ChangeNameViewModel::class)
    internal abstract fun changeNameViewModel(viewModel: ChangeNameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeGenderViewModel::class)
    internal abstract fun changeGenderViewModel(viewModel: ChangeGenderViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEmailViewModel::class)
    internal abstract fun addEmailViewModel(viewModel: AddEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPhoneViewModel::class)
    internal abstract fun addPhoneViewModel(viewModel: AddPhoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewAddPhoneViewModel::class)
    internal abstract fun newAddPhoneViewModel(viewModel: NewAddPhoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddBodViewModel::class)
    internal abstract fun addBodViewModel(viewModel: AddBodViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileInfoViewModel::class)
    internal abstract fun profileInfoViewModel(viewModel: ProfileInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileRoleViewModel::class)
    internal abstract fun profileRoleViewModel(viewModel: ProfileRoleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddChangePinViewModel::class)
    internal abstract fun addChangePinViewModel(viewModel: AddChangePinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePinViewModel::class)
    internal abstract fun changePinViewModel(viewModel: ChangePinViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeBioUsernameViewModel::class)
    internal abstract fun changeBioUsernameViewModel(viewModel: ChangeBioUsernameViewModel): ViewModel

}
