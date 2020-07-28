package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.home.listener.DealsVoucherPlaceCardListener
import com.tokopedia.deals.home.ui.adapter.DealsVoucherPlaceCardAdapter
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.item_deals_voucher_popular_place.view.*

class VoucherPlacePopularViewHolder(
        itemView: View,
        private val voucherPlaceCardListener: DealsVoucherPlaceCardListener
) : BaseViewHolder(itemView) {

    private val voucherPlaceCardAdapter = DealsVoucherPlaceCardAdapter(voucherPlaceCardListener)

    fun bindData(voucherPlacePopular: VoucherPlacePopularDataView) {
        itemView.run {
            txt_voucher_popular_place_title.text = voucherPlacePopular.title

            if (voucherPlacePopular.subtitle.isNotEmpty()) {
                txt_voucher_popular_place_subtitle.text = voucherPlacePopular.subtitle
            } else txt_voucher_popular_place_subtitle.hide()

            lst_voucher_popular_place_card.apply {
                layoutManager = GridLayoutManager(context, VOUCHER_PLACE_SPAN_COUNT)
                adapter = voucherPlaceCardAdapter
                voucherPlaceCardAdapter.voucherPlaceCards = voucherPlacePopular.voucherPlaceCards
            }

            ViewCompat.setNestedScrollingEnabled(lst_voucher_popular_place_card, false)
        }
        voucherPlaceCardListener.onVoucherPlaceCardBind(voucherPlacePopular, adapterPosition)
    }

    companion object {
        private const val VOUCHER_PLACE_SPAN_COUNT = 2

        val LAYOUT: Int = com.tokopedia.deals.R.layout.item_deals_voucher_popular_place
    }
}