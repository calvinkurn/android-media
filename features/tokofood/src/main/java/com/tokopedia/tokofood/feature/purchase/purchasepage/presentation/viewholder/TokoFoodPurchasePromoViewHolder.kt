package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.explorepromo.ExplorePromo
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchasePromoBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel

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
                }
                element.title.isEmpty() || element.description.isEmpty() -> {
                    usePromoAppliedButton.state = ExplorePromo.STATE_DEFAULT
                }
                else -> {
                    usePromoAppliedButton.run {
                        state = ExplorePromo.STATE_APPLIED
                        title.text = element.title
                        description.text = element.description
                    }
                }
            }
            setExplorePromoDescColorProgramatically()
        }
    }

    private fun setExplorePromoDescColorProgramatically() {
        itemView.context?.let {
            val textColor = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
            viewBinding.usePromoAppliedButton.description.setTextColor(textColor)
        }
    }

}