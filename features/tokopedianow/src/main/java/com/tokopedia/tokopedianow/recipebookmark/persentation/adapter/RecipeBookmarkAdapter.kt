package com.tokopedia.tokopedianow.recipebookmark.persentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.recipebookmark.persentation.adapter.differ.RecipeBookmarkDiffer
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeProgressBarUiModel
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.RecipeShimmeringUiModel

class RecipeBookmarkAdapter(
    typeFactory: RecipeBookmarkAdapterTypeFactory
): BaseTokopediaNowListAdapter<Visitable<*>, RecipeBookmarkAdapterTypeFactory>(typeFactory, RecipeBookmarkDiffer()) {

    var isLoadingLoadMore: Boolean = false
        private set

    override fun showLoading() {
        isLoadingLoadMore = true
        val items = getItems()
        items.add(RecipeProgressBarUiModel())
        submitList(items)
    }

    override fun hideLoading() {
        isLoadingLoadMore = false
        val items = getItems()
        if (items.last() is RecipeProgressBarUiModel) {
            items.removeLast()
            submitList(items)
        }
    }

    fun showItemLoading(position: Int, isRemoving: Boolean) {
        val items = getItems()
        if (isRemoving) {
            items.remove(items.getOrNull(position))
            items.add(position, RecipeShimmeringUiModel())
            submitList(items)
        } else {
            items.add(position, RecipeShimmeringUiModel())
            submitList(items)
        }
    }

    private fun getItems(): MutableList<Visitable<*>> {
        return visitables.toMutableList()
    }

}