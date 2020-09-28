package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.dataview.VoucherPlaceCardDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_deals_voucher_place_card.view.*

class VoucherPlaceCardViewHolder(
        itemView: View,
        private val voucherPlaceCardListener: DealsVoucherPlaceCardListener
) : RecyclerView.ViewHolder(itemView) {

    fun bindData(voucherPlaceCard: VoucherPlaceCardDataView) {
        itemView.run {
            img_voucher_place_card.loadImage(
                voucherPlaceCard.imageUrl,
                getLocationPlaceholder(adapterPosition)
            )
            txt_voucher_place_card_name.text = voucherPlaceCard.name

            if (voucherPlaceCard.count.isNotEmpty()) txt_voucher_place_card_count.text = voucherPlaceCard.count
            else txt_voucher_place_card_count.hide()

            setOnClickListener {
                voucherPlaceCardListener.onVoucherPlaceCardClicked(
                        voucherPlaceCard,
                        adapterPosition
                )
            }
        }
    }

    private fun getLocationPlaceholder(position: Int): Int {
        return when (position % LOCATION_PLACEHOLDER_VARIANT) {
            0 -> R.drawable.img_location_placeholder_1
            1 -> R.drawable.img_location_placeholder_2
            2 -> R.drawable.img_location_placeholder_3
            3 -> R.drawable.img_location_placeholder_4
            4 -> R.drawable.img_location_placeholder_5
            else -> R.drawable.img_location_placeholder_6
        }
    }

    companion object {
        const val LOCATION_PLACEHOLDER_VARIANT = 6
    }
}