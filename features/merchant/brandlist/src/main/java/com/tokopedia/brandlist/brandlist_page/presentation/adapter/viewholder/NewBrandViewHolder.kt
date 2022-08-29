package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandUiModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.NewBrandAdapter
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener
import com.tokopedia.unifyprinciples.Typography

class NewBrandViewHolder(itemView: View?, listener: BrandlistPageTrackingListener) :
        AbstractViewHolder<NewBrandUiModel>(itemView) {

    private var context: Context? = null
    private var adapter: NewBrandAdapter? = null
    private var headerView: Typography? = null
    private var recyclerView: RecyclerView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        recyclerView = itemView?.findViewById(R.id.rv_new_brand)

        itemView?.context?.let {
            context = it
            adapter = NewBrandAdapter(it, listener)
            recyclerView?.adapter = adapter
            recyclerView?.layoutManager = GridLayoutManager(it, 3)
        }
    }

    override fun bind(element: NewBrandUiModel?) {

        headerView?.text = element?.header?.title

        element?.newBrands?.let {
            if (recyclerView?.isComputingLayout == false) {
                adapter?.setNewBrands(it)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_new_brand_layout
    }
}