package com.tokopedia.chatbot.chatbot2.attachinvoice.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.chatbot2.attachinvoice.view.fragment.TransactionInvoiceListFragment
import dagger.Component

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = [AttachInvoiceModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AttachInvoiceComponent {

    fun inject(transactionInvoiceListFragment: TransactionInvoiceListFragment)
}
