package com.tokopedia.sellerhome.stub.features.home.di

import com.tokopedia.saldodetails.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.sellerhome.di.module.SellerHomeUseCaseModule
import com.tokopedia.sellerhome.di.module.SellerHomeViewModelModule
import com.tokopedia.commissionbreakdown.di.scope.CommissionBreakdownScope
import com.tokopedia.sellerhome.stub.di.component.BaseAppComponentStub
import dagger.Component

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@CommissionBreakdownScope
@Component(
    modules = [
        SellerHomeModuleStub::class,
        SellerHomeUseCaseModule::class,
        SellerHomeViewModelModule::class
    ],
    dependencies = [BaseAppComponentStub::class]
)
interface SellerHomeComponentStub : CommissionBreakdownComponent