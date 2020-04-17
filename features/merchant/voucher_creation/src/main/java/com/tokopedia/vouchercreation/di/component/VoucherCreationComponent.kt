package com.tokopedia.vouchercreation.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.vouchercreation.di.module.VoucherCreationModule
import com.tokopedia.vouchercreation.di.module.VoucherCreationViewModelModule
import com.tokopedia.vouchercreation.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import dagger.Component

@VoucherCreationScope
@Component(
        modules = [
            VoucherCreationModule::class,
            VoucherCreationViewModelModule::class
        ], dependencies = [BaseAppComponent::class]
)
interface VoucherCreationComponent {

    fun inject(createMerchantVoucherStepsActivity: CreateMerchantVoucherStepsActivity)
}