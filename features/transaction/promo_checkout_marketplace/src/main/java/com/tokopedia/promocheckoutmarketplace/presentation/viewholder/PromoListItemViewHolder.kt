package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoCardBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.unifycomponents.CardUnify

class PromoListItemViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_list_item
    }

    override fun bind(element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiState.isAttempted) {
                textPromoCode.text = element.uiData.promoCode
                textPromoCode.show()
                textPromoCodeInfo.show()
            } else {
                textPromoCode.gone()
                textPromoCodeInfo.gone()
            }

            // set tokopoints/ovopoints cashback info
            renderPromoInfo(element)

            textPromoItemTitle.text = element.uiData.title

            if (element.uiState.isParentEnabled && element.uiData.currentClashingPromo.isNullOrEmpty()) {
                if (element.uiState.isDisabled) {
                    renderDisablePromoItem(element)
                } else {
                    renderEnablePromoItem(element)
                }
            } else {
                renderDisablePromoItem(element)
            }

            cardPromoItem.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION && element.uiData.currentClashingPromo.isNullOrEmpty() && element.uiState.isParentEnabled && !element.uiState.isDisabled) {
                    listener.onClickPromoListItem(element, adapterPosition)
                }
            }
        }
    }

    private fun renderPromoInfo(element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiData.currencyDetailStr.isNotEmpty()) {
                textPromoItemTitleInfo.text = element.uiData.currencyDetailStr

                val params = textPromoItemTitle.layoutParams
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                textPromoItemTitle.layoutParams = params
                textPromoItemTitle.setMargin(0, 0, itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt(), 0)

                // set tokopoints info layout for dynamic position
                val constraintSet = ConstraintSet().apply {
                    clone(containerConstraintPromoCheckout)
                }
                if (textPromoItemTitleInfo.lineCount > 1) {
                    constraintSet.connect(
                            R.id.label_promo_item_title_info,
                            ConstraintSet.TOP,
                            R.id.label_promo_item_title,
                            ConstraintSet.BOTTOM
                    )
                    constraintSet.connect(
                            R.id.label_promo_item_title_info,
                            ConstraintSet.LEFT,
                            R.id.label_promo_item_title,
                            ConstraintSet.LEFT
                    )
                } else {
                    constraintSet.connect(
                            R.id.label_promo_item_title_info,
                            ConstraintSet.TOP,
                            R.id.label_promo_item_title,
                            ConstraintSet.TOP
                    )
                    constraintSet.connect(
                            R.id.label_promo_item_title_info,
                            ConstraintSet.LEFT,
                            R.id.label_promo_item_title,
                            ConstraintSet.RIGHT
                    )
                }
                constraintSet.applyTo(containerConstraintPromoCheckout)

                textPromoItemTitleInfo.show()
            } else {
                val params = textPromoItemTitle.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                textPromoItemTitle.layoutParams = params
                textPromoItemTitle.setMargin(0, 0, itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_12).toInt(), 0)

                textPromoItemTitleInfo.gone()
            }
        }
    }

    private fun renderEnablePromoItem(element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiState.isSelected) {
                cardPromoItem.cardType = CardUnify.TYPE_BORDER_ACTIVE
                imageSelectPromo.show()
            } else {
                cardPromoItem.cardType = CardUnify.TYPE_BORDER
                imageSelectPromo.gone()
            }

            textPromoItemTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            textPromoItemTitleInfo.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            textPromoCodeInfo.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            textPromoCode.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        }
    }

    private fun renderDisablePromoItem(element: PromoListItemUiModel) {
        with(viewBinding) {
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_DISABLED
            imageSelectPromo.gone()

            val disabledColor = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
            textPromoItemTitle.setTextColor(disabledColor)
            textPromoItemTitleInfo.setTextColor(disabledColor)
            textPromoCodeInfo.setTextColor(disabledColor)
            textPromoCode.setTextColor(disabledColor)
        }
    }

}