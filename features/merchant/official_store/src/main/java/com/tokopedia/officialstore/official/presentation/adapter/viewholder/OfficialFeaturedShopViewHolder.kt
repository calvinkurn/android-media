package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.analytics.OfficialStoreTracking
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.officialstore.databinding.ViewmodelOfficialFeaturedShopBinding
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.widget.FeaturedShopAdapter
import com.tokopedia.officialstore.official.presentation.widget.GridSpacingItemDecoration
import com.tokopedia.utils.view.binding.viewBinding

//this is old featured brand from external api
//now doubles with featured brand on dynamic channel
class OfficialFeaturedShopViewHolder(view: View, listener: FeaturedShopListener): AbstractViewHolder<OfficialFeaturedShopDataModel>(view){

    private var adapter: FeaturedShopAdapter? = null

    private var binding: ViewmodelOfficialFeaturedShopBinding? by viewBinding()
    private var officialStoreTracking: OfficialStoreTracking? = null

    override fun bind(element: OfficialFeaturedShopDataModel) {
        if(adapter == null){
            adapter = FeaturedShopAdapter(itemView.context)
            binding?.recyclerViewFeaturedShop?.addItemDecoration(GridSpacingItemDecoration(SPACE_COUNT_2, SPACE_COUNT_8))
            binding?.recyclerViewFeaturedShop?.adapter = adapter
        }

        binding?.linkFeaturedShop?.setOnClickListener {
            officialStoreTracking?.eventClickAllFeaturedBrand(
                    element.categoryName.toEmptyStringIfNull())

            RouteManager.route(it.context, element?.headerShop?.link)
        }

        binding?.linkFeaturedShop?.text = element?.headerShop?.ctaText
        binding?.titleFeaturedShop?.text = element?.headerShop?.title

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
        private const val SPACE_COUNT_2 = 2
        private const val SPACE_COUNT_8 = 8
    }

}