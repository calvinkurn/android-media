package com.tokopedia.settingbank.banklist.v2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.settingbank.banklist.v2.view.fragment.AddBankFragment
import com.tokopedia.settingbank.banklist.v2.view.fragment.SelectBankFragment
import com.tokopedia.settingbank.banklist.v2.view.fragment.SettingBankFragment
import dagger.Component

@SettingBankScope
@Component(modules =
        [SettingBankModule::class,
            ViewModelModule::class,
            GqlRawQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface SettingBankComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(settingBankFragment: SettingBankFragment)
    fun inject(selectBankFragment: SelectBankFragment)
    fun inject(addBankFragment: AddBankFragment)
}
