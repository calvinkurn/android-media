package com.tokopedia.tokofood.feature.purchase.promopage.presentation.viewholder

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoCardBinding
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoActionListener
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel
import com.tokopedia.unifycomponents.CardUnify

class TokoFoodPromoItemViewHolder(private val viewBinding: ItemTokofoodPromoCardBinding,
                                  private val listener: TokoFoodPromoActionListener)
    : AbstractViewHolder<TokoFoodPromoItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_card
    }

    private val colorTextEnabledDefault = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
    private val colorTextEnabledLowEmphasis = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
    private val colorTextDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32)
    private var colorBackgroundSelected = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    private var colorBackgroundDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)

    override fun bind(element: TokoFoodPromoItemUiModel) {
        renderPromoState(viewBinding, element)
        renderPromoData(viewBinding, element)
    }

    private fun renderPromoData(viewBinding: ItemTokofoodPromoCardBinding,
                                element: TokoFoodPromoItemUiModel) {
        with(viewBinding) {
            if (element.isAvailable) {
                textPromoHighlightIdentifierTokofood.text = element.highlightWording
                textPromoHighlightIdentifierTokofood.showWithCondition(element.highlightWording.isNotBlank())
                promoHighlightIdentifierTokofood.showWithCondition(element.highlightWording.isNotBlank())
                imageSelectPromoTokofood.showWithCondition(element.isSelected)
                itemView.setOnClickListener {  }
            } else {
                promoHighlightIdentifierTokofood.gone()
                textPromoHighlightIdentifierTokofood.gone()
                imageSelectPromoTokofood.gone()
                itemView.setOnClickListener {
                    listener.onClickUnavailablePromoItem()
                }
            }

            if (element.additionalInformation.isNotBlank()) {
                containerErrorInfoTokofood.show()
                textErrorInfoTokofood.text = element.additionalInformation
            } else {
                containerErrorInfoTokofood.gone()
            }

            promoBenefitDetail.text = element.title
            textTimeValidityTokofood.text = element.timeValidityWording
        }
    }

    private fun renderPromoState(viewBinding: ItemTokofoodPromoCardBinding,
                                 element: TokoFoodPromoItemUiModel) {
        when {
            element.isAvailable && element.isSelected -> {
                renderPromoSelected(viewBinding)
            }
            element.isAvailable -> {
                renderPromoUnselected(viewBinding, element.additionalInformation.isNotBlank())
            }
            else -> {
                renderPromoDisabled(viewBinding)
            }
        }
    }

    private fun renderPromoSelected(viewBinding: ItemTokofoodPromoCardBinding) {
        with(viewBinding) {
            dividerUserValidityTokofood.show()
            promoBenefitDetail.setTextColor(colorTextEnabledDefault)
            textTimeValidityTokofood.setTextColor(colorTextEnabledLowEmphasis)
            promoCell.cardType = CardUnify.TYPE_BORDER_ACTIVE
            promoCell.setCardBackgroundColor(colorBackgroundSelected)
        }
    }

    private fun renderPromoUnselected(viewBinding: ItemTokofoodPromoCardBinding,
                                      hasAdditionalInformation: Boolean) {
        with(viewBinding) {
            if (hasAdditionalInformation) {
                dividerUserValidityTokofood.gone()
            } else {
                dividerUserValidityTokofood.invisible()
            }
            promoBenefitDetail.setTextColor(colorTextEnabledDefault)
            textTimeValidityTokofood.setTextColor(colorTextEnabledLowEmphasis)
            promoCell.cardType = CardUnify.TYPE_BORDER
        }
    }

    private fun renderPromoDisabled(viewBinding: ItemTokofoodPromoCardBinding) {
        with(viewBinding) {
            dividerUserValidityTokofood.show()
            promoBenefitDetail.setTextColor(colorTextDisabled)
            textTimeValidityTokofood.setTextColor(colorTextDisabled)
            promoCell.cardType = CardUnify.TYPE_BORDER
            promoCell.setCardBackgroundColor(colorBackgroundDisabled)
        }
    }

}
