package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.similarsearch.di.scope.SimilarSearchModuleScope
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SimilarSearchModuleScope
@Module(includes = [
    SimilarSearchUseCaseModule::class,
    WishlistUseCaseModule::class,
    UserSessionModule::class
])
internal class SimilarSearchViewModelFactoryModule {

    @SimilarSearchModuleScope
    @Provides
    @Named(SIMILAR_SEARCH_VIEW_MODEL_FACTORY)
    fun provideSimilarSearchViewModelFactory(
            @Named(GET_SIMILAR_PRODUCT_USE_CASE)
            getSimilarProductsUseCase: UseCase<SimilarProductModel>,
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            userSession: UserSessionInterface
    ): ViewModelProvider.Factory {
        return SimilarSearchViewModelFactory(
                ProductionDispatcherProvider(),
                getSimilarProductsUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                userSession
        )
    }
}