package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.usecase.UseCase as RxUseCase

internal class SimilarSearchViewModelFactory(
        private val dispatcherProvider: CoroutineDispatchers,
        private val similarSearchQuery: String,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>,
        private val addWishlistUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: RxUseCase<AddToCartDataModel>,
        private val userSession: UserSessionInterface
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimilarSearchViewModel::class.java)) {
            return createSimilarSearchViewModel() as T
        }

        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createSimilarSearchViewModel(): SimilarSearchViewModel {
        return SimilarSearchViewModel(
                dispatcherProvider,
                similarSearchQuery,
                getSimilarProductsUseCase,
                addWishlistUseCase,
                removeWishListUseCase,
                addToCartUseCase,
                userSession
        )
    }
}