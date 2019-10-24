package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter
import com.tokopedia.officialstore.official.presentation.widget.GridSpacingItemDecoration

class OfficialFeaturedShopViewHolder(view: View?): AbstractViewHolder<OfficialFeaturedShopViewModel>(view){

    private var recyclerView: RecyclerView? = null
    private var link: TextView? = null

    private var adapter: FeaturedShopAdapter? = null

    init {
        recyclerView = view?.findViewById(R.id.recycler_view_featured_shop)
        link = view?.findViewById(R.id.link_featured_shop)

        view?.context?.let {
            adapter = FeaturedShopAdapter(it)
            recyclerView?.layoutManager = GridLayoutManager(it, 2)
            recyclerView?.addItemDecoration(GridSpacingItemDecoration(2, 8, false))
            recyclerView?.adapter = adapter
        }
    }

    override fun bind(element: OfficialFeaturedShopViewModel?) {
        link?.setOnClickListener {
            // TODO route to applink
        }

        element?.featuredShop?.let {
            adapter?.shopList = it
            adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_featured_shop
    }

}