package com.tokopedia.chatbot.attachinvoice.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.chatbot.attachinvoice.view.fragment.AttachInvoiceFragment
import dagger.Component

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = arrayOf(AttachInvoiceModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AttachInvoiceComponent {

    fun inject(fragment: AttachInvoiceFragment)

}
