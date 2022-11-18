package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.similarsearch.di.SimilarSearchModuleScope
import com.tokopedia.similarsearch.di.UserSessionModule
import com.tokopedia.similarsearch.di.WishlistUseCaseModule
import com.tokopedia.similarsearch.getsimilarproducts.GetSimilarProductsUseCaseModule
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    GetSimilarProductsUseCaseModule::class,
    WishlistUseCaseModule::class,
    UserSessionModule::class
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
            addToWishlistV2UseCase: AddToWishlistV2UseCase,
            deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
            addToCartUseCase: AddToCartUseCase,
            userSession: UserSessionInterface,
            coroutineDispatchers: CoroutineDispatchers
    ): ViewModelProvider.Factory {
        return SimilarSearchViewModelFactory(
                coroutineDispatchers,
                similarSearchQuery,
                getSimilarProductsUseCase,
                addToWishlistV2UseCase,
                deleteWishlistV2UseCase,
                addToCartUseCase,
                userSession
        )
    }
}