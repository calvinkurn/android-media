package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.DealsBrandActionListener
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_deals_brand_home.view.*
import kotlinx.android.synthetic.main.item_deals_brand_popular_item.view.*

/**
 * @author by jessica on 17/06/20
 */

class DealsBrandViewHolder(itemView: View, private val dealsBrandActionListener: DealsBrandActionListener, private val layoutType: Int)
    :RecyclerView.ViewHolder(itemView) {

    fun bind(brand: DealsBrandsDataView.Brand) {
        with(itemView) {
            when(layoutType) {
                LAYOUT -> {
                    imgDealsBrand?.loadImage(brand.image)
                    txtDealsBrand?.text = brand.title
                }
                LAYOUT_WIDE -> {
                    iv_brand?.loadImage(brand.image)
                    brandName?.text = brand.title
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