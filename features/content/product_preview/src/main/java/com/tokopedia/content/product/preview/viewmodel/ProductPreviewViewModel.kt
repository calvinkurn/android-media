package com.tokopedia.content.product.preview.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 06/12/23
 */
class ProductPreviewViewModel @AssistedInject constructor(
    @Assisted private val param: EntrySource,
    private val repo: ProductPreviewRepository
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(param: EntrySource): ProductPreviewViewModel
    }
}
