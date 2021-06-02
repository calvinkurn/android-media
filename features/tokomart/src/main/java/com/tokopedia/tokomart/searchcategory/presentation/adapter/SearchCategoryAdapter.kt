package com.tokopedia.tokomart.searchcategory.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartListAdapter
import com.tokopedia.tokomart.searchcategory.presentation.listener.OnStickySingleHeaderListener
import com.tokopedia.tokomart.searchcategory.presentation.customview.StickySingleHeaderView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.QuickFilterViewHolder

open class SearchCategoryAdapter(
        private val typeFactory: BaseSearchCategoryTypeFactory
):  BaseTokoMartListAdapter<Visitable<*>,
    BaseSearchCategoryTypeFactory>(typeFactory, SearchCategoryDiffUtil()),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    protected open val notFullSpanLayout = listOf(R.layout.item_tokomart_search_category_product)
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
