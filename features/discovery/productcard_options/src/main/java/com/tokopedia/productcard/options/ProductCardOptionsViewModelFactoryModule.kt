package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.di.ProductCardOptionsScope
import com.tokopedia.productcard.options.di.TopAdsWishlistUseCaseModule
import com.tokopedia.productcard.options.di.UserSessionModule
import com.tokopedia.productcard.options.di.WishlistUseCaseModule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [
    WishlistUseCaseModule::class,
    UserSessionModule::class
])
internal class ProductCardOptionsViewModelFactoryModule(
        private val productCardOptionsModel: ProductCardOptionsModel?
) {

    @ProductCardOptionsScope
    @Provides
    @Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)
    fun provideProductCardOptionsViewModelFactory(
            addToWishlistV2UseCase: AddToWishlistV2UseCase,
            deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
            addToCartUseCase: AddToCartUseCase,
            userSession: UserSessionInterface,
            coroutineDispatchers: CoroutineDispatchers
    ): ViewModelProvider.Factory {
        return ProductCardOptionsViewModelFactory(
                coroutineDispatchers,
                productCardOptionsModel,
                addToWishlistV2UseCase,
                deleteWishlistV2UseCase,
                addToCartUseCase,
                userSession
        )
    }
}
