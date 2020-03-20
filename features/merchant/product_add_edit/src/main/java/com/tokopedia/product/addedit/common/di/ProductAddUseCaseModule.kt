package com.tokopedia.product.addedit.common.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.domain.usecase.EditPriceUseCase
import com.tokopedia.product.addedit.common.domain.usecase.ProductAddUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by faisalramd on 2020-03-20.
 */
@Module
class ProductAddUseCaseModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @Provides
    @AddProductQualifier
    fun provideEditPriceUseCase(
            graphqlRepository: GraphqlRepository
    ): EditPriceUseCase {
        return EditPriceUseCase(graphqlRepository)
    }

    @Provides
    @AddProductQualifier
    fun provideProductAddUseCase(
            graphqlRepository: GraphqlRepository
    ): ProductAddUseCase {
        return ProductAddUseCase(graphqlRepository)
    }

}