package com.tokopedia.commissionbreakdown.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.commissionbreakdown.view.CommissionBreakdownActivity
import com.tokopedia.commissionbreakdown.view.CommissionBreakdownFragment
import com.tokopedia.commissionbreakdown.di.module.CommissionBreakdownModule
import com.tokopedia.commissionbreakdown.di.scope.CommissionBreakdownScope
import dagger.Component

@CommissionBreakdownScope
@Component(
    modules = [
        CommissionBreakdownModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CommissionBreakdownComponent {

    fun inject(commissionBreakdownActivity: CommissionBreakdownActivity)

    fun inject(commissionBreakdownFragment: CommissionBreakdownFragment)
}