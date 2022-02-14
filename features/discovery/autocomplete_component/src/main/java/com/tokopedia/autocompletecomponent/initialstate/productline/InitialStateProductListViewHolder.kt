package com.tokopedia.autocompletecomponent.initialstate.productline

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteProductListBinding
import com.tokopedia.utils.view.binding.viewBinding

class InitialStateProductListViewHolder(
    itemView: View,
    clickListener: ProductLineListener,
): AbstractViewHolder<InitialStateProductListDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_product_list
    }

    private val adapter = InitialStateProductListAdapter(clickListener)
    private var binding: LayoutAutocompleteProductListBinding? by viewBinding()

    init {
        binding?.recyclerViewProductList?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(itemView.context)
            recyclerView.layoutManager = layoutManager
            ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
            recyclerView.adapter = adapter
        }
    }

    override fun bind(element: InitialStateProductListDataView) {
        adapter.setData(element.list)
    }
}