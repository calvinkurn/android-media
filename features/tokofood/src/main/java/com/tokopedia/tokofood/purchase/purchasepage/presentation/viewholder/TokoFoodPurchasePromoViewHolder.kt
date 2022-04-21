package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.explorepromo.ExplorePromo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchasePromoBinding
import com.tokopedia.tokofood.databinding.SubItemPurchaseSummaryPromoBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.purchase.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

class TokoFoodPurchasePromoViewHolder(
    private val viewBinding: ItemPurchasePromoBinding,
    private val listener: TokoFoodPurchaseActionListener
) : AbstractViewHolder<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_promo
    }

    override fun bind(element: TokoFoodPurchasePromoTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            usePromoAppliedButton.setOnClickListener {
                listener.onPromoWidgetClicked()
            }
            when {
                element.isLoading -> {
                    usePromoAppliedButton.state = ExplorePromo.STATE_LOADING
                    usePromoAppliedButton.setOnClickListener(null)
                    containerPromoBenefit.gone()
                }
                element.title.isEmpty() || element.description.isEmpty() -> {
                    usePromoAppliedButton.state = ExplorePromo.STATE_DEFAULT
                    containerPromoBenefit.gone()
                }
                else -> {
                    usePromoAppliedButton.run {
                        state = ExplorePromo.STATE_APPLIED
                        title.text = element.title
                        description.text = element.description
                    }
                    containerPromoBenefit.insertPromoBenefits(element.benefitList)
                }
            }
        }
    }

    private fun LinearLayout.insertPromoBenefits(benefitList: List<TokoFoodPurchasePromoTokoFoodPurchaseUiModel.PromoBenefit>) {
        removeAllViews()
        benefitList.forEach {
            val promoBenefitItem =
                SubItemPurchaseSummaryPromoBinding.inflate(LayoutInflater.from(itemView.context))
            promoBenefitItem.textTransactionPromoTitle.text = it.title
            promoBenefitItem.textTransactionPromoValue.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(it.value, false)
                    .removeDecimalSuffix()
            addView(promoBenefitItem.root)
        }
        show()
    }

}