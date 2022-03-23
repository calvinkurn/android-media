package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase

internal class ProductCardOptionsViewModelFactory(
        private val dispatcherProvider: CoroutineDispatchers,
        private val productCardOptionsModel: ProductCardOptionsModel?,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlistUseCase: UseCase<Boolean>,
        private val addToCartUseCase: UseCase<AddToCartDataModel>,
        private val userSession: UserSessionInterface
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductCardOptionsViewModel::class.java)) {
            return createProductCardOptionsViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createProductCardOptionsViewModel(): ProductCardOptionsViewModel {
        return ProductCardOptionsViewModel(
                dispatcherProvider,
                productCardOptionsModel,
                addWishListUseCase,
                removeWishListUseCase,
                topAdsWishlistUseCase,
                addToCartUseCase,
                userSession
        )
    }
}