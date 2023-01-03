package com.tokopedia.mvc.presentation.intro.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcIntroVoucherChoiceOfTargetViewholderBinding
import com.tokopedia.mvc.presentation.intro.uimodel.ChoiceOfVoucherUiModel
import com.tokopedia.mvc.presentation.intro.util.FIRST_INDEX
import com.tokopedia.mvc.presentation.intro.util.ZEROTH_INDEX
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
            element?.list?.getOrNull(ZEROTH_INDEX)?.let {
                viewFlexibleForCoupons.setData(
                    it.benefitTitle,
                    it.benefitSubtitle,
                    it.benefitImageUrl
                )
            }
            element?.list?.getOrNull(FIRST_INDEX)?.let {
                viewSelectionCoupons.setData(
                    it.benefitTitle,
                    it.benefitSubtitle,
                    it.benefitImageUrl
                )
            }
        }
    }
}
