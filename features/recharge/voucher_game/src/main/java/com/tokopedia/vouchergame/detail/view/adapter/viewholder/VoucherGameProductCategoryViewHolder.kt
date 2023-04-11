package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.databinding.ItemVoucherGameDetailCategoryBinding
import com.tokopedia.vouchergame.detail.data.VoucherGameProductData

/**
 * @author by resakemal on 21/08/19
 */

class VoucherGameProductCategoryViewHolder(
    itemView: View
) : AbstractViewHolder<VoucherGameProductData.DataCollection>(itemView) {

    override fun bind(data: VoucherGameProductData.DataCollection) {
        val binding = ItemVoucherGameDetailCategoryBinding.bind(itemView)
        binding.titleLabel.text = data.name
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_voucher_game_detail_category
    }
}
