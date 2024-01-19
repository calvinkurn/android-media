package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.databinding.ItemDealsVoucherPopularPlaceBinding
import com.tokopedia.deals.ui.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.ui.home.ui.adapter.DealsVoucherPlaceCardAdapter
import com.tokopedia.deals.ui.home.ui.dataview.VoucherPlacePopularDataView
import com.tokopedia.kotlin.extensions.view.hide

class VoucherPlacePopularViewHolder(
    itemView: View,
    private val voucherPlaceCardListener: DealsVoucherPlaceCardListener
) : BaseViewHolder(itemView) {

    private val voucherPlaceCardAdapter = DealsVoucherPlaceCardAdapter(voucherPlaceCardListener)

    fun bindData(voucherPlacePopular: VoucherPlacePopularDataView) {

        val binding = ItemDealsVoucherPopularPlaceBinding.bind(itemView)
        binding.run {
            txtVoucherPopularPlaceTitle
                .text = voucherPlacePopular.title

            if (voucherPlacePopular.subtitle.isNotEmpty()) {
                txtVoucherPopularPlaceSubtitle
                    .text = voucherPlacePopular.subtitle
            } else txtVoucherPopularPlaceSubtitle.hide()

            lstVoucherPopularPlaceCard
            .apply {
                layoutManager = GridLayoutManager(context, VOUCHER_PLACE_SPAN_COUNT)
                adapter = voucherPlaceCardAdapter
                voucherPlaceCardAdapter.voucherPlaceCards = voucherPlacePopular.voucherPlaceCards
            }.also {
                    ViewCompat.setNestedScrollingEnabled(it, false)
                }


        }
        itemView.run {

        }
        voucherPlaceCardListener.onVoucherPlaceCardBind(voucherPlacePopular, adapterPosition)
    }

    companion object {
        private const val VOUCHER_PLACE_SPAN_COUNT = 2

        val LAYOUT: Int = com.tokopedia.deals.R.layout.item_deals_voucher_popular_place
    }
}
