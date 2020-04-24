package com.tokopedia.vouchercreation.create.view.viewholder.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.ImageVoucherTipsItemUiModel

class ImageVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<ImageVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_image_info
    }

    override fun bind(element: ImageVoucherTipsItemUiModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}