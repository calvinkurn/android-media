package com.tokopedia.similarsearch.getsimilarproducts

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.similarsearch.GET_SIMILAR_PRODUCT_USE_CASE
import com.tokopedia.similarsearch.di.SimilarSearchModuleScope
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
internal class GetSimilarProductsUseCaseModule(private val productId: String) {

    @SimilarSearchModuleScope
    @Provides
    @Named(GET_SIMILAR_PRODUCT_USE_CASE)
    fun provideGetSimilarProductUseCase(): UseCase<SimilarProductModel> {
        return GetSimilarProductsUseCase(
                productId,
                getSimilarProductsGQLQuery(),
                GraphqlCacheStrategy.Builder(CacheType.NONE).build(),
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }
}