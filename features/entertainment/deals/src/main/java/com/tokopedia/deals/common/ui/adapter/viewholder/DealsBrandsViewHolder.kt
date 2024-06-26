package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.adapter.DealsCommonBrandAdapter
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.databinding.ItemDealsPopularBrandListBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * @author by jessica on 17/06/20
 */

class DealsBrandsViewHolder(itemView: View, private val brandActionListener: DealsBrandActionListener)
    : BaseViewHolder(itemView) {

    fun bind(categories: DealsBrandsDataView) {

        val binding = ItemDealsPopularBrandListBinding.bind(itemView)
        with(binding){
            if (!categories.isLoaded) {
                container.hide()
                shimmering.root.show()
            } else {
                shimmering.root.hide()
                container.show()
                txtDealsPopularBrandTitle.text = categories.title
                txtDealsPopularBrandSeeAll.text = categories.seeAllText
                txtDealsPopularBrandSeeAll.setOnClickListener {
                    brandActionListener.onClickSeeAllBrand(categories.seeAllUrl)
                }

                val adapter = DealsCommonBrandAdapter(brandActionListener, DealsBrandViewHolder.LAYOUT)
                rvDealsBrandPopular.adapter = adapter
                rvDealsBrandPopular.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

                adapter.brandList = categories.brands
            }

            ViewCompat.setNestedScrollingEnabled(rvDealsBrandPopular, false)

        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_popular_brand_list
    }
}
