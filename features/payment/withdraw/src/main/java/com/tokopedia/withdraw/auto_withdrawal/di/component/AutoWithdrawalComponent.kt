package com.tokopedia.withdraw.auto_withdrawal.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.withdraw.auto_withdrawal.di.module.AutoWdModule
import com.tokopedia.withdraw.auto_withdrawal.di.module.ViewModelModule
import com.tokopedia.withdraw.auto_withdrawal.di.scope.AutoWithdrawalScope
import com.tokopedia.withdraw.auto_withdrawal.presentation.activity.AutoWithdrawalActivity
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.AutoWDInfoFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.ExclusiveRekPremFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.fragment.AutoWithdrawalSettingsFragment
import dagger.Component


@AutoWithdrawalScope

@Component(modules = [ViewModelModule::class,
    AutoWdModule::class],
        dependencies = [BaseAppComponent::class])
interface AutoWithdrawalComponent {
    @ApplicationContext
    fun getContext(): Context

    fun inject(activity: AutoWithdrawalActivity)
    fun inject(autoWithdrawalSettingsFragment: AutoWithdrawalSettingsFragment)
    fun inject(exclusiveRekPremFragment: ExclusiveRekPremFragment)
    fun inject(autoWDInfoFragment: AutoWDInfoFragment)
}