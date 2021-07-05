package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter
import com.tokopedia.officialstore.official.presentation.widget.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.viewmodel_official_featured_shop.view.*

class OfficialFeaturedShopViewHolder(view: View, listener: FeaturedShopListener): AbstractViewHolder<OfficialFeaturedShopDataModel>(view){

    private var adapter: FeaturedShopAdapter? = null

    private var officialStoreTracking: OfficialStoreTracking? = null

    override fun bind(element: OfficialFeaturedShopDataModel) {
        if(adapter == null){
            adapter = FeaturedShopAdapter(itemView.context)
            itemView.recycler_view_featured_shop?.addItemDecoration(GridSpacingItemDecoration(2, 8))
            itemView.recycler_view_featured_shop?.adapter = adapter
        }

        itemView.link_featured_shop?.setOnClickListener {
            officialStoreTracking?.eventClickAllFeaturedBrand(
                    element.categoryName.toEmptyStringIfNull())

            RouteManager.route(it.context, element?.headerShop?.link)
        }

        itemView.link_featured_shop?.text = element?.headerShop?.ctaText
        itemView.title_featured_shop?.text = element?.headerShop?.title

        element?.featuredShop?.let {
            adapter?.shopList = it
            adapter?.notifyDataSetChanged()

            it.forEachIndexed { index, shop ->
                element.listener.onShopImpression(
                        element.categoryName,
                        index + 1,
                        shop
                )
            }

            adapter?.onItemClickListener = object: FeaturedShopAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, shop: Shop) {
                    element.listener.onShopClick(
                            element.categoryName,
                            position,
                            shop
                    )
                }

            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_featured_shop
    }

}