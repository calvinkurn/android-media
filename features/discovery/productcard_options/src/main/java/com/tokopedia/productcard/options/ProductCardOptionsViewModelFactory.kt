package com.tokopedia.productcard.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.model.ProductCardOptionsModel

internal class ProductCardOptionsViewModelFactory(
        private val dispatcherProvider: DispatcherProvider,
        private val productCardOptionsModel: ProductCardOptionsModel?
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
                productCardOptionsModel
        )
    }
}