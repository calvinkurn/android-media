package com.tokopedia.attachinvoice.di

import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.usecase.AttachInvoiceUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
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
    fun provideUseCase(repository: GraphqlRepository): CoroutineUseCase<Map<String, Any>, GetInvoiceResponse> {
        return AttachInvoiceUseCase(repository, Dispatchers.IO)
    }

}