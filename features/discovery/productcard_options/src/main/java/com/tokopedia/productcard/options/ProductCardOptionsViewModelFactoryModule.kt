package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.coroutines.ProductionDispatcherProvider
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ProductCardOptionsScope
@Module
internal class ProductCardOptionsViewModelFactoryModule(
        private val productCardOptionsModel: ProductCardOptionsModel?
) {

    @ProductCardOptionsScope
    @Provides
    @Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)
    fun provideProductCardOptionsViewModelFactory(): ViewModelProvider.Factory {
        return ProductCardOptionsViewModelFactory(
                ProductionDispatcherProvider(),
                productCardOptionsModel
        )
    }
}