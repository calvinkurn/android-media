package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ItemDealsVoucherPlaceCardBinding
import com.tokopedia.deals.ui.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.ui.home.ui.dataview.VoucherPlaceCardDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage

class VoucherPlaceCardViewHolder(
    val binding:  ItemDealsVoucherPlaceCardBinding,
    private val voucherPlaceCardListener: DealsVoucherPlaceCardListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(voucherPlaceCard: VoucherPlaceCardDataView) {
        binding.run {
            imgVoucherPlaceCard.loadImage(
                voucherPlaceCard.imageUrl,
                getLocationPlaceholder(adapterPosition)
            )
            txtVoucherPlaceCardName.text = voucherPlaceCard.name

            if (voucherPlaceCard.count.isNotEmpty())
                txtVoucherPlaceCardCount.text = voucherPlaceCard.count
            else
                txtVoucherPlaceCardCount.hide()

            root.setOnClickListener {
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
