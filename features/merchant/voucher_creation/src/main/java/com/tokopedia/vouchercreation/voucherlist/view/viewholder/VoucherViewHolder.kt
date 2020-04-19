package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_list.view.*

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<VoucherUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_mvc_voucher_list
    }

    override fun bind(element: VoucherUiModel) {
        with(itemView) {
            btnMore.setOnClickListener {
                listener.onMoreClickListener(element)
            }
        }
    }

    interface Listener {
        fun onMoreClickListener(voucher: VoucherUiModel)
    }
}