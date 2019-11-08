package com.tokopedia.similarsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.usecase.coroutines.UseCase

internal class SimilarSearchViewModelFactory(
        private val dispatcherProvider: DispatcherProvider,
        private val similarSearchSelectedProduct: SimilarSearchSelectedProduct,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>
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
                similarSearchSelectedProduct,
                getSimilarProductsUseCase
        )
    }
}