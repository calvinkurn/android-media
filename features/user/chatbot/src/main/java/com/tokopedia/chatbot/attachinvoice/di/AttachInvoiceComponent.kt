package com.tokopedia.chatbot.attachinvoice.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.attachinvoice.view.fragment.AttachInvoiceFragment
import com.tokopedia.chatbot.attachinvoice.view.fragment.TransactionInvoiceListFragment
import dagger.Component

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = [AttachInvoiceModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AttachInvoiceComponent {

    fun inject(fragment: AttachInvoiceFragment)
    fun inject(transactionInvoiceListFragment: TransactionInvoiceListFragment)

}
