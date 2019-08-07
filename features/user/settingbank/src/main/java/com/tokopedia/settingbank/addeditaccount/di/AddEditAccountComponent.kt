package com.tokopedia.settingbank.addeditaccount.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.settingbank.addeditaccount.view.fragment.AddEditBankFormFragment
import dagger.Component

/**
 * Created by Ade Fulki on 2019-05-15.
 */

@AddEditAccountScope
@Component(modules = [AddEditAccountModule::class], dependencies = [BaseAppComponent::class])
interface AddEditAccountComponent{

    fun inject(addEditBankFormFragment: AddEditBankFormFragment)
}