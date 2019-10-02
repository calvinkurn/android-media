package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel

class OfficialFeaturedShopViewHolder(view: View?): AbstractViewHolder<OfficialFeaturedShopViewModel>(view){

    private var recyclerView: RecyclerView? = null

    init {
        recyclerView = view?.findViewById(R.id.recycler_view_featured_shop)
    }

    override fun bind(element: OfficialFeaturedShopViewModel?) {

    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_featured_shop
    }

}