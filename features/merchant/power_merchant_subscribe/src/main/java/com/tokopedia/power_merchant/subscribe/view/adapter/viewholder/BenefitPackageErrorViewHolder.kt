package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.GoldMerchantUtil.setTypeGlobalError
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageErrorListener
import kotlinx.android.synthetic.main.item_benefit_package_error.view.*

class BenefitPackageErrorViewHolder(
    view: View,
    private val benefitPackageErrorListener: BenefitPackageErrorListener
) :
    AbstractViewHolder<BenefitPackageErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_error
    }

    override fun bind(element: BenefitPackageErrorUiModel?) {
        with(itemView) {
            globalErrorBenefitError.setTypeGlobalError(element?.throwable)
            globalErrorBenefitError.errorAction.setOnClickListener {
                benefitPackageErrorListener.onErrorActionClicked()
            }
        }
    }
}