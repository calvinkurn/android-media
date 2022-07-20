package com.tokopedia.attachinvoice.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.attachinvoice.view.fragment.AttachInvoiceFragment
import dagger.Component

@AttachInvoiceScope
@Component(
        modules = [AttachInvoiceModule::class, AttachInvoiceViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface AttachInvoiceComponent {
    fun inject(fragment: AttachInvoiceFragment)
}