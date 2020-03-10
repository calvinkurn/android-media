package com.tokopedia.thankyou_native.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.thankyou_native.presentation.fragment.DeferredPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.InstantPaymentFragment
import com.tokopedia.thankyou_native.presentation.fragment.LoaderFragment
import com.tokopedia.thankyou_native.presentation.views.PDPThankYouPageView
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

    fun inject(loaderFragment: LoaderFragment)
    fun inject(instantPaymentFragment: InstantPaymentFragment)
    fun inject(deferredPaymentFragment: DeferredPaymentFragment)
    fun inject(view: PDPThankYouPageView)

}