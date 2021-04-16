package com.tokopedia.autocomplete.initialstate.productline

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class InitialStateProductListViewHolder(
        itemView: View,
        clickListener: InitialStateItemClickListener
): AbstractViewHolder<InitialStateProductListDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_product_list
    }

    private val adapter: InitialStateProductListAdapter

    init {
        val layoutManager = LinearLayoutManager(itemView.context)
        itemView.recyclerView?.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(itemView.recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = InitialStateProductListAdapter(clickListener)
        itemView.recyclerView?.adapter = adapter
    }

    override fun bind(element: InitialStateProductListDataView) {
        adapter.setData(element.list)
    }
}