package com.tokopedia.recharge_credit_card.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.recharge_credit_card.CCBankListBottomSheet
import dagger.Component

@RechargeCCScope
@Component(modules = [RechargeCCModule::class, RechargeCCViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface RechargeCCComponent {

    fun inject(CCBankListBottomSheet: CCBankListBottomSheet)

}