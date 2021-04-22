package com.tokopedia.chatbot.attachinvoice.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Hendri on 05/04/18.
 */
@Module
class AttachInvoiceModule constructor(val context: Context) {

    @AttachInvoiceScope
    @Provides
    fun provideResources(): Resources {
        return context.resources
    }

    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}
