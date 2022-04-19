package com.tokopedia.recharge_credit_card.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.recharge_credit_card.RechargeCCActivity
import com.tokopedia.recharge_credit_card.bottomsheet.CCBankListBottomSheet
import com.tokopedia.recharge_credit_card.RechargeCCFragment
import dagger.Component

@RechargeCCScope
@Component(modules = [RechargeCCModule::class, RechargeCCViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface RechargeCCComponent {

    fun inject(CCBankListBottomSheet: CCBankListBottomSheet)

    fun inject(rechargeCCFragment: RechargeCCFragment)

    fun inject(rechargeCCActivity: RechargeCCActivity)

}