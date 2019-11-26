package com.tokopedia.similarsearch

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SimilarSearchModuleScope
@Module
internal class SimilarSearchUseCaseModule(private val productId: String) {

    @SimilarSearchModuleScope
    @Provides
    @Named(GET_SIMILAR_PRODUCT_USE_CASE)
    fun provideGetSimilarProductUseCase(): UseCase<SimilarProductModel> {
        return GetSimilarProductsUseCase(
                productId,
                getSimilarProductGQLQuery(),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }
}