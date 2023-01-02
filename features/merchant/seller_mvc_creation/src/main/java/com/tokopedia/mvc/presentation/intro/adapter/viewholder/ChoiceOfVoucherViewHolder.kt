package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVoucherChoiceOfTargetViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.ChoiceOfVoucherUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ChoiceOfVoucherViewHolder(itemView: View?) : AbstractViewHolder<ChoiceOfVoucherUiModel>(
    itemView
) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_intro_voucher_choice_of_target_viewholder
    }

    private var binding: SmvcIntroVoucherChoiceOfTargetViewholderBinding? by viewBinding()

    override fun bind(element: ChoiceOfVoucherUiModel?) {
        binding?.apply {
            title.text = element?.title.toBlankOrString()
            element?.list?.getOrNull(0)?.let {
                viewFlexibleForCoupons.setText(it.benefitTitle, it.benefitSubtitle)
            }
            element?.list?.getOrNull(1)?.let {
                viewSelectionCoupons.setText(it.benefitTitle, it.benefitSubtitle)
            }
        }
    }
}
