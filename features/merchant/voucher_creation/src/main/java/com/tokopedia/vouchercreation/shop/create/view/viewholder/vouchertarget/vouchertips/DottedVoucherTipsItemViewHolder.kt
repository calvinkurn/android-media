package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcVoucherTipsDottedInfoBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.DottedVoucherTipsItemUiModel

class DottedVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<DottedVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_dotted_info
    }

    private var binding: MvcVoucherTipsDottedInfoBinding? by viewBinding()

    override fun bind(element: DottedVoucherTipsItemUiModel) {
        binding?.apply {
            voucherTipsDotDesc.text = root.resources?.getString(element.descRes).toBlankOrString()
        }
    }
}