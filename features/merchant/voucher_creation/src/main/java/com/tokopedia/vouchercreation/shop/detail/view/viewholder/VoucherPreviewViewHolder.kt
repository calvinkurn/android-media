package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcVoucherPreviewBinding
import com.tokopedia.vouchercreation.shop.detail.model.VoucherPreviewUiModel

/**
 * Created By @ilhamsuaib on 10/05/20
 */

class VoucherPreviewViewHolder(itemView: View?) : AbstractViewHolder<VoucherPreviewUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_preview
    }

    private var binding: ItemMvcVoucherPreviewBinding? by viewBinding()

    override fun bind(element: VoucherPreviewUiModel) {
        binding?.imgMvcVoucherPreview?.loadImage(element.imgVoucherUrl)
    }
}