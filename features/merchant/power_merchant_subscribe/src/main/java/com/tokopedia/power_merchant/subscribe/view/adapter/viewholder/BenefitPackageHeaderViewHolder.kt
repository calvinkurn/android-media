package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageHeaderUiModel
import kotlinx.android.synthetic.main.item_benefit_package_header.view.*

class BenefitPackageHeaderViewHolder(view: View) :
    AbstractViewHolder<BenefitPackageHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_package_header
    }

    override fun bind(element: BenefitPackageHeaderUiModel?) {
        with(itemView) {
            tvTitleHeaderBenefitPackage?.text = element?.periodDate
            tvDescHeaderBenefitPackage?.text = element?.gradeName?.asCamelCase()
            tvNextUpdateBenefitPackage?.text = element?.nextUpdate
        }
    }
}