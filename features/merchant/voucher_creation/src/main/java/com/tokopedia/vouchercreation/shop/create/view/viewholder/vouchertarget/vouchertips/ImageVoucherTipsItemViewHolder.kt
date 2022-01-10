package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.ImageVoucherTipsItemUiModel
import kotlinx.android.synthetic.main.mvc_voucher_tips_image_info.view.*

class ImageVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<ImageVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_image_info
    }

    override fun bind(element: ImageVoucherTipsItemUiModel) {
        itemView.run {
            voucherTipsInfoImage?.setImageResource(element.iconRes)
            voucherTipsInfoTitle?.text = resources?.getString(element.titleRes).toBlankOrString()
            voucherTipsInfoDesc?.text = resources?.getText(element.descRes) ?: ""
        }
    }
}