package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
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

    private val impressHolder = ImpressHolder()
    private val impressHolderBenefitPM = ImpressHolder()

    override fun bind(element: ItemStatusRMUiModel?) {
        if (element == null) return
        with(itemView) {
            addOnImpressionListener(impressHolder) {
                itemPotentialPowerMerchantListener.onViewRegularMerchantListener(this)
            }
            tv_see_all_benefit_pm?.addOnImpressionListener(impressHolderBenefitPM) {
                itemPotentialPowerMerchantListener.onImpressBenefitSeeAll()
            }
        }
        setItemRegularMerchant(element)
    }

    private fun setItemRegularMerchant(element: ItemStatusRMUiModel) {
        with(itemView) {
            tvTitleEligiblePowerMerchant?.text = getString(R.string.title_eligible_status_power_merchant,
                    element.statusGradePM)
            tvDescEligiblePowerMerchant?.text = getString(R.string.desc_potential_eligible_power_merchant,
                    element.updateDatePotential)
            containerEligiblePowerMerchant?.background = element.bgGradePM?.let { ContextCompat.getDrawable(context, it) }

            iv_pm_badge_eligible_status?.loadImage(element.badgeGradePM)
            tv_pm_potential_value?.text = MethodChecker.fromHtml(getString(R.string.desc_pm_potential))
            tv_see_all_benefit_pm?.setOnClickListener {
                itemPotentialPowerMerchantListener.onItemClickedBenefitPotentialRM()
            }
        }
    }
}