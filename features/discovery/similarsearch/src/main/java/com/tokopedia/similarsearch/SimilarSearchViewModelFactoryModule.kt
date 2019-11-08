package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.similarsearch.di.scope.SimilarSearchModuleScope
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SimilarSearchModuleScope
@Module(includes = [SimilarSearchUseCaseModule::class])
internal class SimilarSearchViewModelFactoryModule(
        private val similarSearchSelectedProduct: SimilarSearchSelectedProduct
) {

    @SimilarSearchModuleScope
    @Provides
    @Named(SIMILAR_SEARCH_VIEW_MODEL_FACTORY)
    fun provideSimilarSearchViewModelFactory(
            @Named(GET_SIMILAR_PRODUCT_USE_CASE)
            getSimilarProductsUseCase: UseCase<SimilarProductModel>
    ): ViewModelProvider.Factory {
        return SimilarSearchViewModelFactory(
                ProductionDispatcherProvider(),
                similarSearchSelectedProduct,
                getSimilarProductsUseCase
        )
    }
}