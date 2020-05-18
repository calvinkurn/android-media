package com.tokopedia.product.addedit.preview.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.preview.domain.mapper.AddProductInputMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ProductAddUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by faisalramd on 2020-03-20.
 */
@Module
class ProductAddUseCaseModule {
    @Provides
    @AddEditProductPreviewScope
    fun provideProductAddUseCase(
            graphqlRepository: GraphqlRepository
    ): ProductAddUseCase {
        return ProductAddUseCase(graphqlRepository)
    }

    @Provides
    @AddEditProductPreviewScope
    fun provideAddProductInputMapper(): AddProductInputMapper {
        return AddProductInputMapper()
    }
}