package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ProductCardOptionsScope
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
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            userSession: UserSessionInterface
    ): ViewModelProvider.Factory {
        return ProductCardOptionsViewModelFactory(
                ProductionDispatcherProvider(),
                productCardOptionsModel,
                addWishListUseCase,
                removeWishListUseCase,
                userSession
        )
    }
}