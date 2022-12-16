package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVoucherViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.IntroCouponUiModel
import com.tokopedia.mvc.util.extension.getIndexAtOrEmpty
import com.tokopedia.utils.view.binding.viewBinding

class IntroCouponViewHolder(itemView: View?) : AbstractViewHolder<IntroCouponUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_voucher_viewholder
    }

    //Todo Rename it to something else
    private var binding: SmvcIntroVoucherViewholderBinding? by viewBinding()

    override fun bind(element: IntroCouponUiModel?) {
        binding?.apply {
            title.text = element?.title.toBlankOrString()
            subtitle.text = element?.subtitle.toBlankOrString()
            element?.list?.getIndexAtOrEmpty(0)?.let {
                viewFlexibleForCoupons.setText(it.benefitTitle, it.benefitSubtitle)
            }
            element?.list?.getIndexAtOrEmpty(1)?.let {
                viewSelectionCoupons.setText(it.benefitTitle, it.benefitSubtitle)
            }
            element?.list?.getIndexAtOrEmpty(2)?.let {
                viewSetTarget.setText(it.benefitTitle, it.benefitSubtitle)
            }
        }
    }
}
