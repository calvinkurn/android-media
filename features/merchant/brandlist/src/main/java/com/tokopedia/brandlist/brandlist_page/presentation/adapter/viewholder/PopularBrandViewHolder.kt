package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandUiModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.widget.PopularBrandAdapter
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener
import com.tokopedia.unifyprinciples.Typography

class PopularBrandViewHolder(itemView: View?, listener: BrandlistPageTrackingListener) : AbstractViewHolder<PopularBrandUiModel>(itemView) {

    private var context: Context? = null
    private var adapter: PopularBrandAdapter? = null
    private var headerView: Typography? = null
    private var recyclerView: RecyclerView? = null

    init {
        headerView = itemView?.findViewById(R.id.tv_header)
        recyclerView = itemView?.findViewById(R.id.rv_popular_brand)

        itemView?.context?.let {
            context = it
            adapter = PopularBrandAdapter(it, listener)
            recyclerView?.adapter = adapter
            recyclerView?.layoutManager = GridLayoutManager(it, 3)
        }
    }

    override fun bind(element: PopularBrandUiModel?) {
        headerView?.text = element?.header?.title
        element?.popularBrands?.let {
            adapter?.setPopularBrands(it)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_popular_brand_layout
    }
}