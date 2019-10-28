package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData
import kotlinx.android.synthetic.main.item_voucher_game_detail_category.view.*

/**
 * @author by resakemal on 21/08/19
 */

class VoucherGameProductCategoryViewHolder(itemView: View) : AbstractViewHolder<VoucherGameProductData.DataCollection>(itemView) {

    override fun bind(data: VoucherGameProductData.DataCollection) {
        with(itemView) {
            title_label.text = data.name
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_voucher_game_detail_category
    }
}