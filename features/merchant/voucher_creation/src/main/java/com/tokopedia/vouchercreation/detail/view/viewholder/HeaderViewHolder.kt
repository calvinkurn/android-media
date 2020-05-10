package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.VoucherHeaderUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_header.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class HeaderViewHolder(itemView: View?) : AbstractViewHolder<VoucherHeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_header
    }

    override fun bind(element: VoucherHeaderUiModel) {
        with(itemView) {
            btnMvcDownload.setOnClickListener {

            }
        }
    }
}