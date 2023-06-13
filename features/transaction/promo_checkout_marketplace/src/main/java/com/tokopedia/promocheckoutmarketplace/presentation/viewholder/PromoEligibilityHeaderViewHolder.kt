package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEligibilityHeaderUiModel

class PromoEligibilityHeaderViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding) :
    AbstractViewHolder<PromoEligibilityHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_eligibility_header
    }

    override fun bind(element: PromoEligibilityHeaderUiModel) {
        renderEligibilityInformation(viewBinding, element)
        renderDivider(viewBinding, element)
    }

    private fun renderEligibilityInformation(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding,
        element: PromoEligibilityHeaderUiModel
    ) {
        with(viewBinding) {
            labelPromoEligibilityHeaderTitle.text = element.uiData.title
            if (element.uiData.subTitle.isNotBlank()) {
                labelPromoEligibilityHeaderSubtitle.text = element.uiData.subTitle
                labelPromoEligibilityHeaderSubtitle.show()
            } else {
                labelPromoEligibilityHeaderSubtitle.hide()
            }
        }
    }

    private fun renderDivider(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding,
        element: PromoEligibilityHeaderUiModel
    ) {
        with(viewBinding) {
            val labelPromoEligibilityHeaderTitleLayoutParam = labelPromoEligibilityHeaderTitle.layoutParams as ViewGroup.MarginLayoutParams
            if (element.uiState.isEnabled) {
                divider.show()
                labelPromoEligibilityHeaderTitleLayoutParam.topMargin = itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_12).toInt()
            } else {
                divider.gone()
                labelPromoEligibilityHeaderTitleLayoutParam.topMargin = 0
            }
        }
    }
}
