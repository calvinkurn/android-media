package com.tokopedia.common_wallet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@CommonWalletScope
@Component(modules = arrayOf(CommonWalletModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface CommonWalletComponent {

}
