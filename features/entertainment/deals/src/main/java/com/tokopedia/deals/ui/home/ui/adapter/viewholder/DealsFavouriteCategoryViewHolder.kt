package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.databinding.ItemDealsVoucherPlaceCardBinding
import com.tokopedia.deals.ui.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.ui.home.ui.dataview.CuratedCategoryDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoryViewHolder(itemView: View, private val listener: DealsFavouriteCategoriesListener)
    : BaseViewHolder(itemView) {

    fun bindData(places: CuratedCategoryDataView.CuratedCategory, position: Int)  {
        val binding = ItemDealsVoucherPlaceCardBinding.bind(itemView)
        binding.run{
            imgVoucherPlaceCard.loadImage(places.imageUrl)

            txtVoucherPlaceCardName.text = places.name
            txtVoucherPlaceCardName.maxLines = 2

            txtVoucherPlaceCardCount.hide()

            root.setOnClickListener {
                listener.onClickFavouriteCategory(
                    places.url,
                    places,
                    position
                )
            }

        }
    }
}
