package com.tokopedia.settingbank.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.settingbank.view.fragment.AccountDocumentFragment
import com.tokopedia.settingbank.view.fragment.AddBankFragment
import com.tokopedia.settingbank.view.fragment.SelectBankFragment
import com.tokopedia.settingbank.view.fragment.SettingBankFragment
import dagger.Component

@SettingBankScope
@Component(modules =
        [SettingBankModule::class,
            ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface SettingBankComponent {

    fun inject(settingBankFragment: SettingBankFragment)
    fun inject(selectBankFragment: SelectBankFragment)
    fun inject(addBankFragment: AddBankFragment)
    fun inject(accountDocumentFragment: AccountDocumentFragment)
}
