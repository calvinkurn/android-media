package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.similarsearch.di.AddToCartUseCaseModule
import com.tokopedia.similarsearch.di.SimilarSearchModuleScope
import com.tokopedia.similarsearch.di.UserSessionModule
import com.tokopedia.similarsearch.di.WishlistUseCaseModule
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCaseModule
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    GetSimilarProductsUseCaseModule::class,
    WishlistUseCaseModule::class,
    UserSessionModule::class,
    AddToCartUseCaseModule::class
])
internal class SimilarSearchViewModelFactoryModule(
        private val similarSearchQuery: String
) {

    @SimilarSearchModuleScope
    @Provides
    @Named(SIMILAR_SEARCH_VIEW_MODEL_FACTORY)
    fun provideSimilarSearchViewModelFactory(
            @Named(GET_SIMILAR_PRODUCT_USE_CASE)
            getSimilarProductsUseCase: UseCase<SimilarProductModel>,
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            addToCartUseCase: AddToCartUseCase,
            userSession: UserSessionInterface
    ): ViewModelProvider.Factory {
        return SimilarSearchViewModelFactory(
                CoroutineDispatchersProvider,
                similarSearchQuery,
                getSimilarProductsUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                addToCartUseCase,
                userSession
        )
    }
}