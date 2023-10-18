package com.tokopedia.home_account.account_settings.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.account_settings.di.module.TkpdPaySettingModule
import com.tokopedia.home_account.account_settings.presentation.fragment.setting.TkpdPaySettingFragment
import dagger.Component

/**
 * @author hadi.putra on 8/2/18.
 */
@ActivityScope
@Component(modules = [TkpdPaySettingModule::class], dependencies = [BaseAppComponent::class])
interface TkpdPaySettingComponent {

    fun inject(fragment: TkpdPaySettingFragment)
}
