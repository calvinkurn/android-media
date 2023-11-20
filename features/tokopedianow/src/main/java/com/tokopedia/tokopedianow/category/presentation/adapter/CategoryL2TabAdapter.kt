package com.tokopedia.tokopedianow.category.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.adapter.differ.CategoryL2TabDiffer
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.CategoryL2TabAdapterTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.view.LinearStickySingleHeaderView
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OnStickySingleHeaderListener

class CategoryL2TabAdapter(
    private val typeFactory: CategoryL2TabAdapterTypeFactory,
    differ: CategoryL2TabDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, BaseAdapterTypeFactory>(typeFactory, differ),
    LinearStickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var onStickySingleHeaderListener: OnStickySingleHeaderListener? = null

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst { it is CategoryQuickFilterUiModel }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderListener = onStickySingleHeaderViewListener
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is CategoryQuickFilterViewHolder) {
            (visitables.getOrNull(stickyHeaderPosition) as? CategoryQuickFilterUiModel)?.let {
                viewHolder.bind(it)
            }
        }
    }
}
