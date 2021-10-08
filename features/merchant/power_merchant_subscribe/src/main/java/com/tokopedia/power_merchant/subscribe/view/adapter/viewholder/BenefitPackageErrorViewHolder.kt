package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.GoldMerchantUtil.setTypeGlobalError
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPackageErrorBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageErrorListener
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding

class BenefitPackageErrorViewHolder(
    view: View,
    private val benefitPackageErrorListener: BenefitPackageErrorListener
) : AbstractViewHolder<BenefitPackageErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_error
    }

    private val binding: ItemBenefitPackageErrorBinding? by viewBinding()

    override fun bind(element: BenefitPackageErrorUiModel?) {
        binding?.run {
            globalErrorBenefitError.setTypeGlobalError(element?.throwable)
            globalErrorBenefitError.errorAction.setOnClickListener {
                benefitPackageErrorListener.onErrorActionClicked()
            }
        }
    }
}