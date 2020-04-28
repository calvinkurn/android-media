package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.DownloadVoucherUiModel

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherViewHolder(itemView: View?) : AbstractViewHolder<DownloadVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_download_voucher
    }

    override fun bind(element: DownloadVoucherUiModel?) {

    }
}