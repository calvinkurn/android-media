package com.tokopedia.tokopedianow.searchcategory.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.OnStickySingleHeaderListener
import com.tokopedia.tokopedianow.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.QuickFilterViewHolder

open class SearchCategoryAdapter(
        private val typeFactory: BaseSearchCategoryTypeFactory
):  BaseTokopediaNowListAdapter<Visitable<*>,
    BaseSearchCategoryTypeFactory>(typeFactory, SearchCategoryDiffUtil()),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    protected open val notFullSpanLayout = listOf(R.layout.item_tokopedianow_search_category_product)
    var onStickySingleHeaderListener: OnStickySingleHeaderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)

        setFullSpan(view, viewType)

        return typeFactory.createViewHolder(view, viewType)
    }

    protected open fun setFullSpan(view: View, viewType: Int) {
        val layoutParams = view.layoutParams
        if (layoutParams !is StaggeredGridLayoutManager.LayoutParams) return
        layoutParams.isFullSpan = isFullSpan(viewType)
    }

    protected open fun isFullSpan(viewType: Int): Boolean {
        return !notFullSpanLayout.contains(viewType)
    }

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst { it is QuickFilterDataView }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderListener = onStickySingleHeaderViewListener
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is QuickFilterViewHolder) {
            (visitables.get(stickyHeaderPosition) as? QuickFilterDataView)?.let {
                viewHolder.bind(it)
            }
        }
    }
}
