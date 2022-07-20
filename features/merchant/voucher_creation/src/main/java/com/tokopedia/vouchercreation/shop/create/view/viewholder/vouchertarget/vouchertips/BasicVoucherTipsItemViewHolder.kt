package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.vouchertips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcVoucherTipsBasicInfoBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.BasicVoucherTipsItemUiModel

class BasicVoucherTipsItemViewHolder(itemView: View) : AbstractViewHolder<BasicVoucherTipsItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_voucher_tips_basic_info
    }

    private var binding: MvcVoucherTipsBasicInfoBinding? by viewBinding()

    override fun bind(element: BasicVoucherTipsItemUiModel) {
        binding?.apply {
            expensesEstimationTicker.tickerTitle = root.resources?.getString(element.titleRes).toBlankOrString()
            expensesEstimationTicker.setTextDescription(root.resources?.getString(element.titleRes).toBlankOrString())
        }
    }
}