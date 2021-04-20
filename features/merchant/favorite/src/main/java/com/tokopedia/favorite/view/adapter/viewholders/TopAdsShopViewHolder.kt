package com.tokopedia.favorite.view.adapter.viewholders

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.favorite.R
import com.tokopedia.favorite.view.adapter.TopAdsShopAdapter
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener
import com.tokopedia.favorite.view.viewmodel.TopAdsShopViewModel

/**
 * @author kulomady on 1/24/17.
 */
class TopAdsShopViewHolder(
        itemView: View,
        private val favoriteClickListener: FavoriteClickListener,
        private val impressionImageLoadedListener: TopAdsShopAdapter.ImpressionImageLoadedListener
) : AbstractViewHolder<TopAdsShopViewModel?>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.favorite_child_favorite_rec_shop
    }

    var recShopRecyclerView: RecyclerView = itemView.findViewById<View>(R.id.rec_shop_recycler_view) as RecyclerView
    private val context: Context = itemView.context

    override fun bind(element: TopAdsShopViewModel?) {
        val topAdsShopAdapter = TopAdsShopAdapter(favoriteClickListener, impressionImageLoadedListener)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recShopRecyclerView.layoutManager = linearLayoutManager
        recShopRecyclerView.setHasFixedSize(true)
        recShopRecyclerView.adapter = topAdsShopAdapter
        element?.adsShopItems?.let { topAdsShopAdapter.setData(it) }
    }

}
