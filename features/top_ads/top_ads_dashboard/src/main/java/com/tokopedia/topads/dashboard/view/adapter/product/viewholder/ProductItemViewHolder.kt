package com.tokopedia.topads.dashboard.view.adapter.product.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.dashboard.R
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

    override fun bind(item: ProductItemViewModel, selectMode: Boolean) {
        item.let {
            if (selectMode) {
                view.btn_switch.visibility = View.GONE
                view.check_box.visibility = View.VISIBLE
            } else {
                view.card_view?.setCardBackgroundColor(view.context.resources.getColor(R.color.white))
                view.btn_switch.visibility = View.VISIBLE
                view.check_box.visibility = View.GONE
            }
            view.check_box.isChecked = item.isChecked
            view.btn_switch.isChecked = it.data.adStatus == 1
            ImageLoader.LoadImage(view.product_img, it.data.productImageUri)
            view.product_name.text = it.data.productName
            view.tampil_count.text = it.data.statTotalImpression
            view.klik_count.text = it.data.statTotalClick
            view.persentase_klik_count.text = it.data.statTotalClick
            view.pengeluaran_count.text = it.data.statTotalSpent
            view.pendapatan_count.text = it.data.statTotalConversion
            view.produk_terjual_count.text = it.data.statTotalSold
            view.label.visibility = View.GONE
            view.img_menu.visibility = View.GONE
            view.progress_layout.visibility = View.GONE
            view.btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                onSwitchAction.invoke(adapterPosition, isChecked)
            }
            view.item_card.setOnClickListener {
                if (selectMode) {
                    view.check_box.isChecked = !it.check_box.isChecked
                    item.isChecked = view.check_box.isChecked
                    if (view.check_box.isChecked)
                        view.card_view.setCardBackgroundColor(view.context.resources.getColor(R.color.topads_select_color))
                    else
                        view.card_view?.setCardBackgroundColor(view.context.resources.getColor(R.color.white))
                }
            }

            view.item_card.setOnLongClickListener {
                view.check_box.isChecked = true
                item.isChecked = true
                view.card_view.setCardBackgroundColor(view.context.resources.getColor(R.color.topads_select_color))
                onSelectMode.invoke(true)
                true
            }

        }
    }

}