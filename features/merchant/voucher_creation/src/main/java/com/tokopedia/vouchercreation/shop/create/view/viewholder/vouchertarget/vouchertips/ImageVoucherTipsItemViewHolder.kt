package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcVoucherTipsImageInfoBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.ImageVoucherTipsItemUiModel

class ImageVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<ImageVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_image_info
    }

    private var binding: MvcVoucherTipsImageInfoBinding? by viewBinding()

    override fun bind(element: ImageVoucherTipsItemUiModel) {
        binding?.apply {
            voucherTipsInfoImage.setImageResource(element.iconRes)
            voucherTipsInfoTitle.text = root.resources?.getString(element.titleRes).toBlankOrString()
            voucherTipsInfoDesc.text = root.resources?.getText(element.descRes) ?: ""
        }
    }
}