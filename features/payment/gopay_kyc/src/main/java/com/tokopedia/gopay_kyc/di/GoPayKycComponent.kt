package com.tokopedia.gopay_kyc.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gopay_kyc.di.module.GoPayKycModule
import com.tokopedia.gopay_kyc.di.module.ViewModelModule
import dagger.Component

@GoPayKycScope
@Component(
    modules = [GoPayKycModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface GoPayKycComponent {

}