package com.tokopedia.topads.dashboard.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_ACTIVE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.STATUS_TIDAK_TAMPIL
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemViewModel
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.*
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.card_view
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.check_box
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.img_menu
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.item_card
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.klik_count
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.label
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.pendapatan_count
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.pengeluaran_count
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.persentase_klik_count
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.produk_terjual_count
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.progress_layout
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.tampil_count
import kotlinx.android.synthetic.main.topads_dash_item_with_group_card.view.*

/**
 * Created by Pika on 7/6/20.
 */


class ProductItemViewHolder(val view: View,
                            var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
                            var onSelectMode: ((select: Boolean) -> Unit)) : ProductViewHolder<ProductItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    override fun bind(item: ProductItemViewModel, selectMode: Boolean, statsData: MutableList<WithoutGroupDataItem>) {
        item.let {
            view.img_menu.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.ic_topads_menu))
            if (selectMode) {
                view.btn_switch.visibility = View.GONE
                view.check_box.visibility = View.VISIBLE
            } else {
                view.card_view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                view.btn_switch.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
            }
            view.check_box.isChecked = item.isChecked
            view.btn_switch.setOnCheckedChangeListener(null)
            if (!item.isChanged)
                view.btn_switch.isChecked = it.data.adStatus == STATUS_ACTIVE || it.data.adStatus == STATUS_TIDAK_TAMPIL
            else
                view.btn_switch.isChecked = item.valueChanged
            view.product_img.setImageUrl(it.data.productImageUri)
            view.product_name.text = it.data.productName
            if (statsData.isNotEmpty() && adapterPosition < statsData.size) {
                view.tampil_count.text = statsData[adapterPosition].statTotalImpression
                view.klik_count.text = statsData[adapterPosition].statTotalClick
                view.persentase_klik_count.text = statsData[adapterPosition].statTotalCtr
                view.pengeluaran_count.text = statsData[adapterPosition].statTotalSpent
                view.produk_terjual_count.text = statsData[adapterPosition].statTotalConversion
            }
            view.pendapatan_count.text = it.data.statTotalGrossProfit
            view.label.visibility = View.INVISIBLE
            view.img_menu.visibility = View.INVISIBLE
            view.progress_layout.visibility = View.GONE
            view.btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                item.isChanged = true
                item.valueChanged = isChecked
                onSwitchAction.invoke(adapterPosition, isChecked)
            }
            if (!view.check_box.isChecked) {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
            } else {
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
            }
            view.item_card.setOnClickListener {
                if (selectMode) {
                    view.check_box.isChecked = !it.check_box.isChecked
                    item.isChecked = view.check_box.isChecked
                    if (view.check_box.isChecked)
                        view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                    else
                        view.card_view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_dash_white))
                }
            }

            view.item_card.setOnLongClickListener {
                view.check_box.isChecked = true
                item.isChecked = true
                view.card_view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.topads_select_color))
                onSelectMode.invoke(true)
                true
            }

        }
    }

}