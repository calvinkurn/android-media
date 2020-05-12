package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel
import kotlinx.android.synthetic.main.item_mvc_share_voucher.view.*

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherViewHolder(
        itemView: View?,
        private val onClick: (ShareVoucherUiModel) -> Unit
) : AbstractViewHolder<ShareVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_share_voucher
    }

    override fun bind(element: ShareVoucherUiModel) {
        with(itemView) {
            tvMvcSocmed.text = element.socmedName
            icMvcSocmed.loadImageDrawable(element.icon)

            setOnClickListener {
                onClick(element)
            }
        }
    }
}