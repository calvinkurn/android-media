package com.tokopedia.thankyou_native.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.thankyou_native.analytics.ThankYouPageAnalytics
import com.tokopedia.thankyou_native.di.scope.ThankYouPageScope
import com.tokopedia.thankyou_native.presentation.adapter.DetailedInvoiceAdapter
import com.tokopedia.thankyou_native.presentation.adapter.InvoiceTypeFactory
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@ThankYouPageScope
@Module
class ThankYouPageModule {

    @ThankYouPageScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @ThankYouPageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ThankYouPageScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }


    @ThankYouPageScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ThankYouPageScope
    @Provides
    fun provideThankYouPageAnalytics(): ThankYouPageAnalytics {
        return ThankYouPageAnalytics()
    }

    @ThankYouPageScope
    @Provides
    fun provideInvoiceTypeFactory() : InvoiceTypeFactory{
        return InvoiceTypeFactory()
    }



    @ThankYouPageScope
    @Provides
    fun provideDetailInvoiceAdapter(invoiceTypeFactory: InvoiceTypeFactory): DetailedInvoiceAdapter {
        return DetailedInvoiceAdapter(arrayListOf(), invoiceTypeFactory)
    }

}