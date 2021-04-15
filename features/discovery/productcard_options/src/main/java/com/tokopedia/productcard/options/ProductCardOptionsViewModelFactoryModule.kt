package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.di.*
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    WishlistUseCaseModule::class,
    TopAdsWishlistUseCaseModule::class,
    AddToCartUseCaseModule::class,
    UserSessionModule::class
])
internal class ProductCardOptionsViewModelFactoryModule(
        private val productCardOptionsModel: ProductCardOptionsModel?
) {

    @ProductCardOptionsScope
    @Provides
    @Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)
    fun provideProductCardOptionsViewModelFactory(
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            topAdsWishlistUseCase: UseCase<Boolean>,
            addToCartUseCase: AddToCartUseCase,
            userSession: UserSessionInterface,
            coroutineDispatchers: CoroutineDispatchers
    ): ViewModelProvider.Factory {
        return ProductCardOptionsViewModelFactory(
                coroutineDispatchers,
                productCardOptionsModel,
                addWishListUseCase,
                removeWishListUseCase,
                topAdsWishlistUseCase,
                addToCartUseCase,
                userSession
        )
    }
}