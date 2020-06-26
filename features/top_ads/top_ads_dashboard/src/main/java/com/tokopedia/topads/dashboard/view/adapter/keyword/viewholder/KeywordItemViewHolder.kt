package com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_dash_item_keyword_card.view.*

/**
 * Created by Pika on 7/6/20.
 */

class KeywordItemViewHolder(val view: View,
                            private var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
                            private var onSelectMode: ((select: Boolean) -> Unit)) : KeywordViewHolder<KeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_keyword_card
    }

    override fun bind(item: KeywordItemViewModel, selectMode: Boolean, fromSearch: Boolean) {
        item.let {
            if (selectMode) {
                view.btn_switch.visibility = View.GONE
                view.check_box.visibility = View.VISIBLE
            } else {
                view.card_view?.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
                view.btn_switch.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
            }
            view.check_box.isChecked = item.isChecked
            view.key_title.text = it.result.keywordTag
            view.btn_switch.isChecked = it.result.keywordStatus == STATUS_ACTIVE || it.result.keywordStatus == STATUS_TIDAK_TAMPIL
            view.label.setLabelType(Label.GENERAL_LIGHT_GREEN)
            view.label.text = it.result.keywordTypeDesc
            view.tampil_count.text = it.result.statTotalImpression
            view.klik_count.text = it.result.statTotalClick
            view.persentase_klik_count.text = it.result.statTotalClick
            view.pengeluaran_count.text = it.result.statTotalSpent
            view.pendapatan_count.text = it.result.statTotalConversion
            view.produk_terjual_count.text = it.result.statTotalSold
            view.price_bid.text = it.result.keywordPriceBidFmt
            view.btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                onSwitchAction.invoke(adapterPosition, isChecked)
            }

            view.item_card.setOnClickListener {
                if (selectMode) {
                    view.check_box.isChecked = !view.check_box.isChecked
                    item.isChecked = view.check_box.isChecked
                    if (view.check_box.isChecked)
                        view.card_view?.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                    else
                        view.card_view?.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
                }
            }

            view.item_card.setOnLongClickListener {
                item.isChecked = true
                view.check_box.isChecked = true
                view.card_view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                onSelectMode.invoke(true)
                true
            }

        }
    }

}