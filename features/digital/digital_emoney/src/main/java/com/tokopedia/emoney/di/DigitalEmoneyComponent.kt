package com.tokopedia.emoney.di


import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceNFCFragment

import dagger.Component

@DigitalEmoneyScope
@Component(modules = [DigitalEmoneyModule::class, DigitalEmoneyViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface DigitalEmoneyComponent {

    fun inject(emoneyCheckBalanceNFCFragment: EmoneyCheckBalanceNFCFragment)
}
