package com.tokopedia.accountprofile.settingprofile.addname.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.accountprofile.settingprofile.addname.fragment.AddNameRegisterPhoneFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import dagger.Component

/**
 * @author by nisie on 23/04/19.
 */
@AddNameScope
@SessionCommonScope
@Component(modules = [AddNameModule::class, AddNameViewModelModule::class], dependencies = arrayOf(BaseAppComponent::class))
interface AddNameComponent {

    fun inject(fragment: AddNameRegisterPhoneFragment)
}
