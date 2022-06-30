package com.tokopedia.createpost.producttag.view.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.createpost.producttag.util.AUTHOR_ID
import com.tokopedia.createpost.producttag.util.AUTHOR_TYPE
import com.tokopedia.createpost.producttag.util.PRODUCT_TAG_SOURCE_RAW
import com.tokopedia.createpost.producttag.util.SHOP_BADGE
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
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
        ): ProductTagViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return productTagViewModelFactory.create(
            productTagSourceRaw,
            shopBadge,
            authorId,
            authorType,
        ) as T
    }
}