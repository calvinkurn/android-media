package com.tokopedia.emoney.di

import com.tokopedia.common_electronic_money.di.NfcCheckBalanceComponent
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceFragment
import dagger.Component

@DigitalEmoneyScope
@Component(modules = [DigitalEmoneyModule::class, DigitalEmoneyViewModelModule::class],
        dependencies = [NfcCheckBalanceComponent::class])
interface DigitalEmoneyComponent {

    fun inject(emoneyCheckBalanceFragment: EmoneyCheckBalanceFragment)
}
