package com.tokopedia.wallet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wallet.ovoactivation.view.ActivationOvoFragment
import com.tokopedia.wallet.ovoactivation.view.IntroOvoFragment
import com.tokopedia.wallet.ovoactivationflashdeals.InactiveOvoFragment
import dagger.Component

@WalletScope
@Component(modules = arrayOf(WalletModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface WalletComponent {

    fun inject(activationOvoFragment: ActivationOvoFragment)

    fun inject(introOvoFragment: IntroOvoFragment)

    fun inject(inactiveOvoFragment: InactiveOvoFragment)

}