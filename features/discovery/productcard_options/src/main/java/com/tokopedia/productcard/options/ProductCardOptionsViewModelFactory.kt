package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Lazy

internal class ProductCardOptionsViewModelFactory(
    private val dispatcherProvider: CoroutineDispatchers,
    private val productCardOptionsModel: ProductCardOptionsModel?,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val addToCartUseCase: UseCase<AddToCartDataModel>,
    private val userSession: UserSessionInterface,
    private val similarSearchCoachMarkLocalCache: SimilarSearchCoachMarkLocalCache,
    private val abTestRemoteConfig: Lazy<RemoteConfig>,
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductCardOptionsViewModel::class.java)) {
            return createProductCardOptionsViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createProductCardOptionsViewModel(): ProductCardOptionsViewModel {
        return ProductCardOptionsViewModel(
            dispatcherProvider,
            productCardOptionsModel,
            addToWishlistV2UseCase,
            deleteWishlistV2UseCase,
            addToCartUseCase,
            userSession,
            similarSearchCoachMarkLocalCache,
            abTestRemoteConfig,
        )
    }
}
