package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoEligibilityHeaderUiModel
import com.tokopedia.unifyprinciples.Typography

class PromoEligibilityHeaderViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoEligibilityHeaderBinding,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoEligibilityHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_eligibility_header
    }

    override fun bind(element: PromoEligibilityHeaderUiModel) {
        with(viewBinding) {
            labelPromoEligibilityHeaderTitle.text = element.uiData.title
            if (element.uiData.subTitle.isNotBlank()) {
                labelPromoEligibilityHeaderSubtitle.text = element.uiData.subTitle
                labelPromoEligibilityHeaderSubtitle.show()
            } else {
                labelPromoEligibilityHeaderSubtitle.hide()
            }

            if (element.uiState.isEnabled) {
                itemView.setOnClickListener { }
            } else {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) listener.onClickPromoEligibilityHeader(element)
                }
            }
        }
    }

}