package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialRegularMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusRMUiModel
import kotlinx.android.synthetic.main.item_potential_eligible_status_regular_merchant.view.*

class ItemStatusRMViewHolder(view: View,
                             private val itemPotentialPowerMerchantListener: ItemPotentialRegularMerchantListener) :
        AbstractViewHolder<ItemStatusRMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_eligible_status_regular_merchant
    }

    private val impressHolderBenefitPM = ImpressHolder()

    override fun bind(element: ItemStatusRMUiModel?) {
        if (element == null) return
        with(itemView) {
            tv_see_all_benefit_pm?.addOnImpressionListener(impressHolderBenefitPM) {
                itemPotentialPowerMerchantListener.onImpressBenefitSeeAll()
            }
        }
        setItemRegularMerchant(element)
    }

    private fun setItemRegularMerchant(element: ItemStatusRMUiModel) {
        with(itemView) {
            tvTitleEligiblePowerMerchant?.text = getString(R.string.title_header_rm_section)
            tvDescEligiblePowerMerchant?.text = getString(R.string.desc_potential_eligible_power_merchant,
                    element.updateDatePotential)
            containerEligiblePowerMerchant?.background = ContextCompat.getDrawable(context, R.drawable.bg_header_bronze)

            tv_pm_potential_value?.text = MethodChecker.fromHtml(getString(R.string.desc_pm_potential))
            tv_see_all_benefit_pm?.setOnClickListener {
                itemPotentialPowerMerchantListener.onItemClickedBenefitPotentialRM()
            }
        }
    }
}