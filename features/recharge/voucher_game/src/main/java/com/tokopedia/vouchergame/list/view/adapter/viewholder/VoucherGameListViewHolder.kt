package com.tokopedia.vouchergame.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import kotlinx.android.synthetic.main.item_voucher_game.view.*

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameListViewHolder(val view: View, val listener: OnClickListener) : AbstractViewHolder<VoucherGameOperator>(view) {

    override fun bind(operator: VoucherGameOperator) {
        with(itemView) {
            ImageHandler.LoadImage(item_image, operator.attributes.imageUrl)
            product_title.text = operator.attributes.name
            if (operator.attributes.operatorLabel.isNotEmpty()) {
                promo_label.visibility = View.VISIBLE
                promo_label.text = operator.attributes.operatorLabel.joinToString(limit = 2)
            }

            item_container.setOnClickListener { listener.onItemClicked(operator) }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_game
    }

    interface OnClickListener {
        fun onItemClicked(operator: VoucherGameOperator)
    }

}