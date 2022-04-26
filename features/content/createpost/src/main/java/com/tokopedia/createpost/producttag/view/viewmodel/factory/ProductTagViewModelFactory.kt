package com.tokopedia.createpost.producttag.view.viewmodel.factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tokopedia.createpost.producttag.view.viewmodel.ProductTagViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagViewModelFactory @AssistedInject constructor(
    @Assisted activity: FragmentActivity,
    @Assisted("productTagSourceRaw") private val productTagSourceRaw: String,
    @Assisted("shopBadge") private val shopBadge: String,
    @Assisted("authorId") private val authorId: String,
    @Assisted("authorType") private val authorType: String,
    private val productTagViewModelFactory: ProductTagViewModel.Factory,
) : AbstractSavedStateViewModelFactory(activity, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            activity: FragmentActivity,
            @Assisted("productTagSourceRaw") productTagSourceRaw: String,
            @Assisted("shopBadge") shopBadge: String,
            @Assisted("authorId") authorId: String,
            @Assisted("authorType") authorType: String,
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