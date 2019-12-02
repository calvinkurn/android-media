package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter
import com.tokopedia.officialstore.official.presentation.widget.GridSpacingItemDecoration
import com.tokopedia.unifyprinciples.Typography

class OfficialFeaturedShopViewHolder(view: View): AbstractViewHolder<OfficialFeaturedShopViewModel>(view){

    private var context: Context? = null
    private var recyclerView: RecyclerView? = null
    private var link: AppCompatTextView? = null
    private var title: Typography? = null

    private var adapter: FeaturedShopAdapter? = null

    private var officialStoreTracking: OfficialStoreTracking? = null

    init {
        recyclerView = view.findViewById(R.id.recycler_view_featured_shop)
        link = view.findViewById(R.id.link_featured_shop)
        title = view.findViewById(R.id.title_featured_shop)

        view.context?.let {
            context = it
            officialStoreTracking = OfficialStoreTracking(it)
            adapter = FeaturedShopAdapter(it)
            recyclerView?.layoutManager = GridLayoutManager(it, 2)
            recyclerView?.addItemDecoration(GridSpacingItemDecoration(2, 8))
            recyclerView?.adapter = adapter
        }
    }

    override fun bind(element: OfficialFeaturedShopViewModel?) {
        link?.setOnClickListener {
            officialStoreTracking?.eventClickAllFeaturedBrand(
                    element?.categoryName.toEmptyStringIfNull())

            RouteManager.route(it.context, element?.headerShop?.link)
        }

        link?.text = element?.headerShop?.ctaText
        title?.text = element?.headerShop?.title

        element?.featuredShop?.let {
            adapter?.shopList = it
            adapter?.notifyDataSetChanged()

            it.forEachIndexed { index, shop ->
                officialStoreTracking?.eventImpressionFeatureBrand(
                        element.categoryName.toEmptyStringIfNull(),
                        shop.shopId.toString().toEmptyStringIfNull(),
                        index + 1,
                        shop.name.toEmptyStringIfNull(),
                        shop.url.toEmptyStringIfNull(),
                        shop.additionalInformation.toEmptyStringIfNull()
                )
            }

            adapter?.onItemClickListener = object: FeaturedShopAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, shop: Shop) {
                    officialStoreTracking?.eventClickFeaturedBrand(
                            element.categoryName.toEmptyStringIfNull(),
                            shop.shopId.toEmptyStringIfNull(),
                            position,
                            shop.name.toEmptyStringIfNull(),
                            shop.url.toEmptyStringIfNull(),
                            shop.additionalInformation.toEmptyStringIfNull()
                    )

                    RouteManager.route(context, shop.url)
                }

            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.viewmodel_official_featured_shop
    }

}