package com.tokopedia.attachinvoice.di

import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class AttachInvoiceModule {

    @AttachInvoiceScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AttachInvoiceScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @AttachInvoiceScope
    @Provides
    fun provideGqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetInvoiceResponse> {
        return GraphqlUseCase(graphqlRepository)
    }

}