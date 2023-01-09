package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVoucherTypeViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.VoucherTypeUiModel
import com.tokopedia.mvc.util.constant.FIRST_INDEX
import com.tokopedia.mvc.util.constant.ZEROTH_INDEX
import com.tokopedia.utils.view.binding.viewBinding

class VoucherTypeViewHolder(itemView: View?) : AbstractViewHolder<VoucherTypeUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_voucher_type_viewholder
    }

    private var binding: SmvcIntroVoucherTypeViewholderBinding? by viewBinding()

    override fun bind(element: VoucherTypeUiModel?) {
        binding?.apply {
            title.text = element?.title.toBlankOrString()
            element?.list?.getOrNull(ZEROTH_INDEX)?.let {
                viewVoucherShop.setData(it.benefitTitle, it.benefitSubtitle, it.benefitImageUrl)
            }
            element?.list?.getOrNull(FIRST_INDEX)?.let {
                viewVoucherProduct.setData(it.benefitTitle, it.benefitSubtitle, it.benefitImageUrl)
            }
        }
    }
}
