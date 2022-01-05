package com.tokopedia.dg_transaction.testing.di

import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutScope
import com.tokopedia.digital_checkout.di.DigitalCheckoutViewModelModule
import dagger.Component

@DigitalCheckoutScope
@Component(
    modules = [StubDigitalCheckoutModule::class, DigitalCheckoutViewModelModule::class],
    dependencies = [StubDigitalCommonComponent::class]
)
interface StubDigitalCheckoutComponent: DigitalCheckoutComponent