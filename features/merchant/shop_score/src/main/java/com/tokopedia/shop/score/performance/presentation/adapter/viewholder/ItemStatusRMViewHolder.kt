package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPotentialEligibleStatusRegularMerchantBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialRegularMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusRMUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class ItemStatusRMViewHolder(
    view: View,
    private val itemPotentialPowerMerchantListener: ItemPotentialRegularMerchantListener
) :
    AbstractViewHolder<ItemStatusRMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_eligible_status_regular_merchant
    }

    private val binding: ItemPotentialEligibleStatusRegularMerchantBinding? by viewBinding()

    override fun bind(element: ItemStatusRMUiModel?) {
        if (element == null) return
        setItemRegularMerchant(element)
        setupBackgroundColor()
    }

    private fun setItemRegularMerchant(element: ItemStatusRMUiModel) {
        binding?.run {
            tvTitleEligiblePowerMerchant.text =
                element.titleRMEligible?.let { getString(it) }.orEmpty()
            tvDescEligiblePowerMerchant.text =
                element.descRMEligible?.let { getString(it) }.orEmpty()

            tvPmPotentialValue.text =
                MethodChecker.fromHtml(getString(R.string.desc_pm_potential))
            tvSeeAllBenefitPm.setOnClickListener {
                itemPotentialPowerMerchantListener.onItemClickedBenefitPotentialRM()
            }
            if (tvSeeAllBenefitPm.isVisible) {
                itemPotentialPowerMerchantListener.onImpressBenefitSeeAll()
            }
        }
    }

    private fun setupBackgroundColor() {
        binding?.run {
            containerEligiblePowerMerchant.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
            ivBgEligibleRm.showWithCondition(!root.context.isDarkMode())
        }
    }
}