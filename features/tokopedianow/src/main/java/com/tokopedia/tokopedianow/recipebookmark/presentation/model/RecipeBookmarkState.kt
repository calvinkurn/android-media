package com.tokopedia.tokopedianow.recipebookmark.presentation.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tokopedia.abstraction.base.view.adapter.Visitable

sealed class RecipeBookmarkState {

    data class Show(
        val items: SnapshotStateList<Visitable<*>>,
        val scrollToTop: Boolean = false
    ) : RecipeBookmarkState()

    data class Error(
        val throwable: Throwable? = null,
        val code: String? = null
    ) : RecipeBookmarkState()

    object Loading : RecipeBookmarkState()
    object Empty : RecipeBookmarkState()
}
