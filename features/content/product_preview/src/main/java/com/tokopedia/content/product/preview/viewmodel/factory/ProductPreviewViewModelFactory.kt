package com.tokopedia.content.product.preview.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewViewModelFactory @AssistedInject constructor(
    @Assisted private val productPreviewSourceModel: ProductPreviewSourceModel,
    private val factory: ProductPreviewViewModel.Factory
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(@Assisted productPreviewSourceModel: ProductPreviewSourceModel): ProductPreviewViewModelFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(productPreviewSourceModel) as T
}
