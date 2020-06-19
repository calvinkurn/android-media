package com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.autoads.viewmodel.AutoAdsItemsItemViewModel
import kotlinx.android.synthetic.main.topads_dash_item_non_group_card.view.*

/**
 * Created by Pika on 2/6/20.
 */

class AutoAdsItemsItemViewHolder(val view: View) : AutoAdsItemsViewHolder<AutoAdsItemsItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_non_group_card
    }

    override fun bind(item: AutoAdsItemsItemViewModel) {
        item.let {
            ImageLoader.LoadImage(view.product_img, it.data.productImageUri)
            view.product_name.text = it.data.productName
            view.img_menu.visibility = View.GONE
            view.check_box.visibility = View.GONE
            view.label.visibility = View.GONE
            view.btn_switch.visibility = View.GONE
            view.progress_layout.visibility = View.GONE
            view.product_name.text = it.data.productName
            view.tampil_count.text = it.data.statTotalImpression
            view.klik_count.text = it.data.statTotalClick
            view.persentase_klik_count.text = it.data.statTotalCtr
            view.pengeluaran_count.text = it.data.statTotalSpent
            view.pendapatan_count.text = it.data.statTotalConversion
            view.produk_terjual_count.text = it.data.statTotalSold
        }
    }
}