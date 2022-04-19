package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoCardBinding
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel
import com.tokopedia.unifycomponents.CardUnify

class TokoFoodPromoItemViewHolder(private val viewBinding: ItemTokofoodPromoCardBinding,
                                  private val listener: TokoFoodPromoActionListener)
    : AbstractViewHolder<TokoFoodPromoItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_card
    }

    private val colorTextEnabledDefault = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
    private val colorTextEnabledLowEmphasis = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
    private val colorTextDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
    private var colorBackgroundSelected = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    private var colorBackgroundDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)

    override fun bind(element: TokoFoodPromoItemUiModel) {
        renderPromoState(viewBinding, element)
        renderPromoData(viewBinding, element)
    }

    private fun renderPromoData(viewBinding: ItemTokofoodPromoCardBinding,
                                element: TokoFoodPromoItemUiModel) {
        with(viewBinding) {
            if (element.isUnavailable) {
                promoHighlightIdentifierTokofood.gone()
                textPromoHighlightIdentifierTokofood.gone()
                if (element.unavailableInformation.isNotBlank()) {
                    containerErrorInfoTokofood.show()
                    textErrorInfoTokofood.text = element.unavailableInformation
                } else {
                    containerErrorInfoTokofood.gone()
                }
                imageSelectPromoTokofood.gone()
                itemView.setOnClickListener {
                    listener.onClickUnavailablePromoItem()
                }
            } else {
                textPromoHighlightIdentifierTokofood.text = element.highlightWording
                textPromoHighlightIdentifierTokofood.show()
                promoHighlightIdentifierTokofood.show()
                containerErrorInfoTokofood.gone()
                imageSelectPromoTokofood.show()
                itemView.setOnClickListener {  }
            }

            textPromoItemTitleTokofood.text = element.title
            textTimeValidityTokofood.text = element.timeValidityWording
        }
    }

    private fun renderPromoState(viewBinding: ItemTokofoodPromoCardBinding,
                                 element: TokoFoodPromoItemUiModel) {
        if (element.isUnavailable) {
            renderPromoDisabled(viewBinding)
        } else {
            renderPromoEnabled(viewBinding)
        }
    }

    private fun renderPromoEnabled(viewBinding: ItemTokofoodPromoCardBinding) {
        with(viewBinding) {
            textPromoItemTitleTokofood.setTextColor(colorTextEnabledDefault)
            textTimeValidityTokofood.setTextColor(colorTextEnabledLowEmphasis)
            cardPromoItemTokofood.cardType = CardUnify.TYPE_BORDER_ACTIVE
            cardPromoItemTokofood.setCardBackgroundColor(colorBackgroundSelected)
        }
    }

    private fun renderPromoDisabled(viewBinding: ItemTokofoodPromoCardBinding) {
        with(viewBinding) {
            textPromoItemTitleTokofood.setTextColor(colorTextDisabled)
            textTimeValidityTokofood.setTextColor(colorTextDisabled)
            cardPromoItemTokofood.cardType = CardUnify.TYPE_BORDER
            cardPromoItemTokofood.setCardBackgroundColor(colorBackgroundDisabled)
        }
    }

}