package com.tokopedia.vouchergame.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.JvmMediaLoader.loadImage
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.databinding.ItemVoucherGameBinding
import com.tokopedia.vouchergame.list.data.VoucherGameOperator

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameListViewHolder(val view: View, val listener: OnClickListener) : AbstractViewHolder<VoucherGameOperator>(view) {

    override fun bind(operator: VoucherGameOperator) {
        val binding = ItemVoucherGameBinding.bind(itemView)
        loadImage(binding.itemImage, operator.attributes.imageUrl)

        binding.productTitle.text = operator.attributes.name
        if (operator.attributes.operatorLabel.isNotEmpty()) {
            binding.promoLabel.visibility = View.VISIBLE
            binding.promoLabel.text = operator.attributes.operatorLabel.joinToString(limit = 2)
        }

        binding.itemContainer.setOnClickListener { listener.onItemClicked(operator) }
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_game
    }

    interface OnClickListener {
        fun onItemClicked(operator: VoucherGameOperator)
    }
}
