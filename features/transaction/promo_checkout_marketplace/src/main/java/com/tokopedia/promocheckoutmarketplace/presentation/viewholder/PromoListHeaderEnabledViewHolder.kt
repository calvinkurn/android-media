package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoListHeaderEnabledBinding
import com.tokopedia.promocheckoutmarketplace.presentation.IconHelper
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListHeaderUiModel
import com.tokopedia.purchase_platform.common.utils.Utils

class PromoListHeaderEnabledViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoListHeaderEnabledBinding,
                                       private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_list_header_enabled
    }

    override fun bind(element: PromoListHeaderUiModel) {
        with(viewBinding) {
            if (element.uiData.iconUnify.isNotBlank()) {
                if (IconHelper.isIconFromUrl(element.uiData.iconUnify)) {
                    iconPromoListHeader.gone()
                    if (element.uiData.iconUrl.isNotEmpty()) {
                        imagePromoListHeader.setImageUrl(element.uiData.iconUrl)
                        imagePromoListHeader.show()
                    } else {
                        imagePromoListHeader.gone()
                    }
                } else {
                    imagePromoListHeader.gone()
                    iconPromoListHeader.setImage(IconHelper.getIcon(element.uiData.iconUnify))
                    iconPromoListHeader.show()
                }
            } else {
                iconPromoListHeader.gone()
                imagePromoListHeader.gone()
            }

            labelPromoListHeaderTitle.text = Utils.getHtmlFormat(element.uiData.title)
            if (element.uiState.hasSelectedPromoItem) {
                labelPromoListHeaderSubTitle.text =
                    itemView.context.getString(R.string.label_promo_selectable_template, itemView.context.getString(R.string.label_subtitle_promo_selected))
            } else {
                labelPromoListHeaderSubTitle.text =  itemView.context.getString(R.string.label_promo_selectable_template, element.uiData.selectablePromoMessage)
                labelPromoListHeaderSubTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
            }

            labelPromoListHeaderSubTitle.show()
        }
    }

}