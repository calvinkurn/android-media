package com.tokopedia.topads.dashboard.view.adapter.autoads.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.image.ImageLoader
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
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

    override fun bind(item: AutoAdsItemsItemViewModel, statsData: MutableList<WithoutGroupDataItem>) {
        item.let {
            ImageLoader.LoadImage(view.product_img, it.data.productImageUri)
            view.product_name.text = it.data.productName
            view.img_menu.visibility = View.GONE
            view.check_box.visibility = View.GONE
            view.label.visibility = View.INVISIBLE
            view.btn_switch.visibility = View.GONE
            view.progress_layout.visibility = View.GONE
            view.product_name.text = it.data.productName

            if (statsData.isNotEmpty() && adapterPosition < statsData.size) {
                view.tampil_count.text = statsData[adapterPosition].statTotalImpression
                view.klik_count.text = statsData[adapterPosition].statTotalClick
                view.persentase_klik_count.text = statsData[adapterPosition].statTotalCtr
                view.pengeluaran_count.text = statsData[adapterPosition].statTotalSpent
                view.pendapatan_count.text = statsData[adapterPosition].statTotalConversion
                view.produk_terjual_count.text = statsData[adapterPosition].statTotalSold
            }

        }
    }
}