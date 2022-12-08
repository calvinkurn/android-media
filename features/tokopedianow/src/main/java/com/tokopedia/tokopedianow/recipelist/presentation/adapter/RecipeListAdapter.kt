package com.tokopedia.tokopedianow.recipelist.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.view.LinearStickySingleHeaderView
import com.tokopedia.tokopedianow.recipelist.presentation.adapter.differ.RecipeListDiffer
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeFilterViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OnStickySingleHeaderListener

class RecipeListAdapter(
    private val typeFactory: RecipeListAdapterTypeFactory
) : BaseTokopediaNowListAdapter<Visitable<*>, RecipeListAdapterTypeFactory>(
    typeFactory,
    RecipeListDiffer()
), LinearStickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var onStickySingleHeaderListener: OnStickySingleHeaderListener? = null

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst { it is RecipeFilterUiModel }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderListener = onStickySingleHeaderViewListener
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is RecipeFilterViewHolder) {
            (visitables.getOrNull(stickyHeaderPosition) as? RecipeFilterUiModel)?.let {
                viewHolder.bind(it)
            }
        }
    }
}