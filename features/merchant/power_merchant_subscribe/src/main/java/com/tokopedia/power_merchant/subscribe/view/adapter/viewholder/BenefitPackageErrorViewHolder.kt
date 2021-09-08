package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.gm.common.utils.GoldMerchantUtil.setTypeGlobalError
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageErrorListener
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel

class BenefitPackageErrorViewHolder(
    view: View,
    private val benefitPackageErrorListener: BenefitPackageErrorListener
) : AbstractViewHolder<BenefitPackageErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_error
    }

    private val globalErrorBenefitError: GlobalError =
        itemView.findViewById(R.id.globalErrorBenefitError)

    override fun bind(element: BenefitPackageErrorUiModel?) {
        with(itemView) {
            globalErrorBenefitError.setTypeGlobalError(element?.throwable)
            globalErrorBenefitError.errorAction.setOnClickListener {
                benefitPackageErrorListener.onErrorActionClicked()
            }
        }
    }
}