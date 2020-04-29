package com.tokopedia.thankyou_native.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.thankyou_native.di.module.GqlQueryModule
import com.tokopedia.thankyou_native.di.module.ThankYouPageModule
import com.tokopedia.thankyou_native.di.scope.ThankYouPageScope
import com.tokopedia.thankyou_native.di.module.ViewModelModule
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import com.tokopedia.thankyou_native.presentation.fragment.*
import dagger.Component

@ThankYouPageScope
@Component(modules =
[ThankYouPageModule::class,
    ViewModelModule::class,
    GqlQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface ThankYouPageComponent {
    @ApplicationContext
    fun context(): Context

    fun inject(activity: ThankYouPageActivity)
    fun inject(loaderFragment: LoaderFragment)
    fun inject(instantPaymentFragment: InstantPaymentFragment)
    fun inject(deferredPaymentFragment: DeferredPaymentFragment)
    fun inject(processingPaymentFragment: ProcessingPaymentFragment)
    fun inject(invoiceFragment: InvoiceFragment)
    fun inject(cashOnDeliveryFragment: CashOnDeliveryFragment)
}