package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.databinding.ItemDealsBrandHomeBinding
import com.tokopedia.deals.databinding.ItemDealsBrandPopularItemBinding
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 17/06/20
 */

class DealsBrandViewHolder(itemView: View, private val dealsBrandActionListener: DealsBrandActionListener, private val layoutType: Int)
    :RecyclerView.ViewHolder(itemView) {

    fun bind(brand: DealsBrandsDataView.Brand) {
        with(itemView) {
            when(layoutType) {
                LAYOUT -> {
                    val bind = ItemDealsBrandPopularItemBinding.bind(itemView)
                    bind.imgDealsBrand?.loadImage(brand.image)
                    bind.txtDealsBrand?.text = brand.title
                }
                LAYOUT_WIDE -> {
                    val bind = ItemDealsBrandHomeBinding.bind(itemView)
                    bind.ivBrand?.loadImage(brand.image)
                    bind.brandName?.text = brand.title
                }
            }
            setOnClickListener { dealsBrandActionListener.onClickBrand(brand, adapterPosition) }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_deals_brand_popular_item
        val LAYOUT_WIDE = R.layout.item_deals_brand_home
    }

}