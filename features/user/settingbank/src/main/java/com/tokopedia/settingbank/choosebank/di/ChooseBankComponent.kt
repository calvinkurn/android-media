package com.tokopedia.settingbank.choosebank.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.settingbank.choosebank.view.fragment.ChooseBankFragment
import dagger.Component

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

@ChooseBankScope
@Component(modules = [ChooseBankModule::class], dependencies = [BaseAppComponent::class])
interface ChooseBankComponent{

    fun inject(chooseBankFragment: ChooseBankFragment)
}