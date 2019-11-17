package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase

internal class SimilarSearchViewModelFactory(
        private val dispatcherProvider: DispatcherProvider,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>,
        private val addWishlistUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
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
                getSimilarProductsUseCase,
                addWishlistUseCase,
                removeWishListUseCase,
                userSession
        )
    }
}