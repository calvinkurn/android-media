package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_deals_voucher_place_card.view.*

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoryViewHolder(itemView: View, private val listener: DealsFavouriteCategoriesListener)
    : BaseViewHolder(itemView) {

    fun bindData(places: CuratedCategoryDataView.CuratedCategory, position: Int)  {
        itemView.run {
            img_voucher_place_card.loadImage(places.imageUrl)

            txt_voucher_place_card_name.text = places.name
            txt_voucher_place_card_name.maxLines = 2

            txt_voucher_place_card_count.hide()

            setOnClickListener {
                listener.onClickFavouriteCategory(
                        places.url,
                        places,
                        position
                )
            }
        }
    }
}