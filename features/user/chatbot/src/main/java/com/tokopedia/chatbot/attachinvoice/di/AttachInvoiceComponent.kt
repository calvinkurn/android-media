package com.tokopedia.chatbot.attachinvoice.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Component

/**
 * Created by Hendri on 05/04/18.
 */

@AttachInvoiceScope
@Component(modules = arrayOf(AttachInvoiceModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface AttachInvoiceComponent {

//    fun inject(fragment: AttachInvoiceFragment)

    @ApplicationContext
    fun context(): Context
}
