package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class BenefitPackageHeaderViewHolder(view: View) :
    AbstractViewHolder<BenefitPackageHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_header
    }

    private val tvTitleHeaderBenefitPackage: Typography =
        itemView.findViewById(R.id.tvTitleHeaderBenefitPackage)
    private val containerHeaderBenefitPackage: ConstraintLayout =
        itemView.findViewById(R.id.containerHeaderBenefitPackage)
    private val tvDescHeaderBenefitPackage: Typography =
        itemView.findViewById(R.id.tvDescHeaderBenefitPackage)
    private val tvNextUpdateBenefitPackage: Typography =
        itemView.findViewById(R.id.tvNextUpdateBenefitPackage)

    override fun bind(element: BenefitPackageHeaderUiModel?) {
        with(itemView) {
            containerHeaderBenefitPackage.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N50
                )
            )
            tvTitleHeaderBenefitPackage.text =
                getString(R.string.pm_date_based_on_your_sell, element?.periodDate)
            tvDescHeaderBenefitPackage.text = getString(
                R.string.pm_title_profit_package_section,
                element?.gradeName?.asCamelCase()
            )
            tvNextUpdateBenefitPackage.text =
                MethodChecker.fromHtml(
                    getString(
                        R.string.pm_next_update_benefit_package,
                        element?.nextUpdate
                    )
                )
        }
    }
}