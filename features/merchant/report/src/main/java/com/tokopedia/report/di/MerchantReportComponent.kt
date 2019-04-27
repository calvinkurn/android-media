package com.tokopedia.report.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.report.view.fragment.ProductReportFragment
import dagger.Component

@MerchantReportScope
@Component(modules = [MerchantReportModule::class], dependencies = [BaseAppComponent::class])
interface MerchantReportComponent {
    fun inject(fragment: ProductReportFragment)
}