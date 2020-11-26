package com.tokopedia.chatbot.attachinvoice.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * Created by Hendri on 05/04/18.
 */
@AttachInvoiceScope
@Module
class AttachInvoiceModule constructor(val context: Context) {

    @AttachInvoiceScope
    @Provides
    fun provideResources(): Resources {
        return context.resources
    }
}
