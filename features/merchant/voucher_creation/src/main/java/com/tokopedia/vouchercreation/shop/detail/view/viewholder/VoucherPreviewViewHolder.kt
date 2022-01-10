package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.VoucherPreviewUiModel
import kotlinx.android.synthetic.main.item_mvc_voucher_preview.view.*

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class VoucherPreviewViewHolder(itemView: View?) : AbstractViewHolder<VoucherPreviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_preview
    }

    override fun bind(element: VoucherPreviewUiModel) {
        with(itemView) {
            imgMvcVoucherPreview.loadImage(element.imgVoucherUrl)
        }
    }
}