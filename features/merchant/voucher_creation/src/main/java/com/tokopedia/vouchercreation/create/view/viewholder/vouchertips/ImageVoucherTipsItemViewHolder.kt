package com.tokopedia.vouchercreation.create.view.viewholder.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.ImageVoucherTipsItemUiModel
import kotlinx.android.synthetic.main.mvc_voucher_tips_image_info.view.*

class ImageVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<ImageVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_image_info
    }

    override fun bind(element: ImageVoucherTipsItemUiModel) {
        itemView.run {
            ContextCompat.getDrawable(context, element.iconRes)?.let { iconDrawable ->
                voucherTipsInfoImage?.setImageDrawable(iconDrawable)
            }
            voucherTipsInfoTitle?.text = resources?.getString(element.titleRes).toBlankOrString()
            voucherTipsInfoDesc?.text = resources?.getText(element.descRes) ?: ""
        }
    }
}