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
class AttachInvoiceModule {

    @AttachInvoiceScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}
