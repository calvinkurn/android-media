package com.tokopedia.content.product.picker.ugc.view.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.content.product.picker.ugc.util.AUTHOR_ID
import com.tokopedia.content.product.picker.ugc.util.AUTHOR_TYPE
import com.tokopedia.content.product.picker.ugc.util.INITIAL_SELECTED_PRODUCT
import com.tokopedia.content.product.picker.ugc.util.PRODUCT_TAG_CONFIG
import com.tokopedia.content.product.picker.ugc.util.PRODUCT_TAG_SOURCE_RAW
import com.tokopedia.content.product.picker.ugc.util.SHOP_BADGE
import com.tokopedia.content.product.picker.ugc.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.product.picker.ugc.view.viewmodel.ProductTagViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagViewModelFactory @AssistedInject constructor(
        @Assisted owner: SavedStateRegistryOwner,
        @Assisted(PRODUCT_TAG_SOURCE_RAW) private val productTagSourceRaw: String,
        @Assisted(SHOP_BADGE) private val shopBadge: String,
        @Assisted(AUTHOR_ID) private val authorId: String,
        @Assisted(AUTHOR_TYPE) private val authorType: String,
        @Assisted(INITIAL_SELECTED_PRODUCT) private val initialSelectedProduct: List<SelectedProductUiModel>,
        @Assisted(PRODUCT_TAG_CONFIG) private val productTagConfig: ContentProductTagConfig,
        private val productTagViewModelFactory: ProductTagViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
                owner: SavedStateRegistryOwner,
                @Assisted(PRODUCT_TAG_SOURCE_RAW) productTagSourceRaw: String,
                @Assisted(SHOP_BADGE) shopBadge: String,
                @Assisted(AUTHOR_ID) authorId: String,
                @Assisted(AUTHOR_TYPE) authorType: String,
                @Assisted(INITIAL_SELECTED_PRODUCT) initialSelectedProduct: List<SelectedProductUiModel>,
                @Assisted(PRODUCT_TAG_CONFIG) productTagConfig: ContentProductTagConfig,
        ): ProductTagViewModelFactory
    }

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return productTagViewModelFactory.create(
            productTagSourceRaw,
            shopBadge,
            authorId,
            authorType,
            initialSelectedProduct,
            productTagConfig,
        ) as T
    }
}
