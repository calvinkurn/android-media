package com.tokopedia.common_wallet.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase
import dagger.Component

@CommonWalletScope
@Component(modules = arrayOf(CommonWalletModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface CommonWalletComponent {

}