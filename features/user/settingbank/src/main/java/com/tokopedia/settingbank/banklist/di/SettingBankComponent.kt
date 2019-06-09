package com.tokopedia.settingbank.banklist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.settingbank.banklist.view.fragment.SettingBankFragment
import dagger.Component

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

@SettingBankScope
@Component(modules = [SettingBankModule::class], dependencies = [BaseAppComponent::class])
interface SettingBankComponent{

    fun inject(settingBankFragment: SettingBankFragment)
}