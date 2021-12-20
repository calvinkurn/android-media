package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoCardBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class PromoListItemViewHolder(private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                              private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_card
    }

    private val colorTextEnabledDefault = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
    private val colorTextEnabledLowEmphasis = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
    private val colorTextDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
    private var colorBackgroundSelected = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    private var colorBackgroundEnabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
    private var colorBackgroundDisabled = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)

    override fun bind(element: PromoListItemUiModel) {
        if (element.uiState.isUpdateState) {
            element.uiState.isUpdateState = false
            renderPromoState(viewBinding, element)
        } else {
            renderPromoState(viewBinding, element)
            renderPromoData(viewBinding, element)
            setPromoItemClickListener(viewBinding, element)
        }
    }

    private fun setPromoItemClickListener(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                          element: PromoListItemUiModel) {
        viewBinding.cardPromoItem.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION && element.uiData.currentClashingPromo.isNullOrEmpty() &&
                    element.uiState.isParentEnabled && !element.uiState.isDisabled) {
                listener.onClickPromoListItem(element, adapterPosition)
            }
        }
    }

    private fun renderPromoState(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                 element: PromoListItemUiModel) {
        if (element.uiState.isParentEnabled && element.uiData.currentClashingPromo.isNullOrEmpty()) {
            if (element.uiState.isDisabled) {
                renderPromoDisabled(viewBinding)
            } else {
                if (element.uiState.isSelected) {
                    renderPromoSelected(viewBinding)
                } else {
                    renderPromoEnabled(viewBinding)
                }
            }
        } else {
            renderPromoDisabled(viewBinding)
        }
    }

    private fun renderPromoSelected(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding) {
        renderPromoEnabled(viewBinding)
        with(viewBinding) {
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_selected)
            containerUserValidity.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN100))
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_ACTIVE
            cardPromoItem.setCardBackgroundColor(colorBackgroundSelected)
            imageSelectPromo.show()
        }
    }

    private fun renderPromoEnabled(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding) {
        with(viewBinding) {
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_enabled)
            textPromoHighlightIdentifier.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            promoQuantityIdentifierTop.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_top_enabled)
            promoQuantityIdentifierBottom.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_bottom_enabled)
            textPromoQuantity.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            textPromoItemTitle.setTextColor(colorTextEnabledDefault)
            textPromoItemTitleInfo.setTextColor(colorTextEnabledLowEmphasis)
            textPromoCodeInfo.setTextColor(colorTextEnabledDefault)
            textPromoCode.setTextColor(colorTextEnabledDefault)
            textTimeValidity.setTextColor(colorTextEnabledLowEmphasis)
            textUserValidity.setTextColor(colorTextEnabledLowEmphasis)
            containerUserValidity.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
            cardPromoItem.cardType = CardUnify.TYPE_BORDER
            cardPromoItem.setCardBackgroundColor(colorBackgroundEnabled)
            imageSelectPromo.gone()
        }
    }

    private fun renderPromoDisabled(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding) {
        with(viewBinding) {
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_disabled)
            textPromoHighlightIdentifier.setTextColor(colorTextDisabled)
            promoQuantityIdentifierTop.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_top_disabled)
            promoQuantityIdentifierBottom.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_bottom_disabled)
            textPromoQuantity.setTextColor(colorTextDisabled)
            textPromoItemTitle.setTextColor(colorTextDisabled)
            textPromoItemTitleInfo.setTextColor(colorTextDisabled)
            textPromoCodeInfo.setTextColor(colorTextDisabled)
            textPromoCode.setTextColor(colorTextDisabled)
            textTimeValidity.setTextColor(colorTextDisabled)
            textUserValidity.setTextColor(colorTextDisabled)
            containerUserValidity.setBackgroundColor(colorBackgroundDisabled)
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_DISABLED
            cardPromoItem.setCardBackgroundColor(colorBackgroundDisabled)
            imageSelectPromo.gone()
        }
    }

    private fun renderPromoData(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                element: PromoListItemUiModel) {
        renderHighlightIdentifier(viewBinding, element)
        renderQuantityIdentifier(viewBinding, element)
        renderTitle(viewBinding, element)
        renderBenefit(viewBinding, element)
        renderPromoCodeIdentifier(viewBinding, element)
        renderPromoInfo(viewBinding, element)
        renderTimeValidity(viewBinding, element)
        renderUserValidity(viewBinding, element)
        renderClashInfo(viewBinding, element)
    }

    private fun renderHighlightIdentifier(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                          element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiState.isHighlighted) {
                val topBanner = element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                if (topBanner != null) {
                    textPromoHighlightIdentifier.text = topBanner.title
                    textPromoHighlightIdentifier.show()
                    promoHighlightIdentifier.show()
                } else {
                    textPromoHighlightIdentifier.gone()
                    promoHighlightIdentifier.gone()
                }
            } else {
                textPromoHighlightIdentifier.gone()
                promoHighlightIdentifier.gone()
            }
        }
    }

    private fun renderQuantityIdentifier(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                         element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiData.remainingPromoCount > 1) {
                textPromoQuantity.text = "Sisa ${element.uiData.remainingPromoCount}"
                textPromoQuantity.show()
                promoQuantityIdentifierTop.show()
                promoQuantityIdentifierBottom.show()

                val promoQuantityIdentifierLayoutParam = promoQuantityIdentifierTop.layoutParams as ViewGroup.MarginLayoutParams
                val topBanner = element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                if (element.uiState.isHighlighted && topBanner != null) {
                    promoQuantityIdentifierLayoutParam.topMargin = itemView.context.resources.getDimension(R.dimen.dp_32).toInt()
                } else {
                    promoQuantityIdentifierLayoutParam.topMargin = itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_16).toInt()
                }
            } else {
                textPromoQuantity.gone()
                promoQuantityIdentifierTop.gone()
                promoQuantityIdentifierBottom.gone()
            }
        }
    }

    private fun renderTitle(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                            element: PromoListItemUiModel) {
        with(viewBinding) {
            textPromoItemTitle.text = element.uiData.title
            val textPromoItemTitleLayoutParam = textPromoItemTitle.layoutParams as ViewGroup.MarginLayoutParams
            val topBanner = element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            if (element.uiState.isHighlighted && topBanner != null) {
                textPromoItemTitleLayoutParam.topMargin = 0
            } else {
                textPromoItemTitleLayoutParam.topMargin = itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8).toInt()
            }
        }
    }

    private fun renderBenefit(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                              element: PromoListItemUiModel) {
        with(viewBinding) {
            val textPromoItemTitleInfoLayoutParam = textPromoItemTitleInfo.layoutParams as ViewGroup.MarginLayoutParams
            val topBanner = element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            if (element.uiState.isHighlighted && topBanner != null) {
                textPromoItemTitleInfoLayoutParam.topMargin = 0
            } else {
                textPromoItemTitleInfoLayoutParam.topMargin = itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8).toInt()
            }

            if (element.uiData.currencyDetailStr.isNotBlank()) {
                textPromoItemTitleInfo.text = element.uiData.currencyDetailStr
                textPromoItemTitleInfo.show()
            } else {
                textPromoItemTitleInfo.gone()
            }
        }
    }

    private fun renderPromoCodeIdentifier(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                          element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiState.isAttempted) {
                textPromoCode.text = element.uiData.promoCode
                textPromoCode.show()
                textPromoCodeInfo.show()
            } else {
                textPromoCode.gone()
                textPromoCodeInfo.gone()
            }
        }
    }

    private fun renderPromoInfo(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiData.promoInfos.isNotEmpty()) {
                containerPromoInfoList.removeAllViews()
                val promoInfoList = element.uiData.promoInfos.filter { it.type == PromoInfo.TYPE_PROMO_INFO }
                promoInfoList.forEach {
                    val promoInfoView = View.inflate(itemView.context, R.layout.promo_checkout_marketplace_module_sub_layout_promo_info, null)
                            as ConstraintLayout
                    val imagePromoInfo = promoInfoView.findViewById<ImageUnify>(R.id.image_promo_info)
                    imagePromoInfo.setImageUrl(it.icon)
                    val textPromoInfo = promoInfoView.findViewById<Typography>(R.id.text_promo_info)
                    textPromoInfo.text = HtmlLinkHelper(itemView.context, it.title).spannedString
                    if (!element.uiState.isParentEnabled || element.uiState.isDisabled || element.uiData.clashingInfoMessage.isNotBlank()) {
                        textPromoInfo.setTextColor(colorTextDisabled)
                    } else {
                        textPromoInfo.setTextColor(colorTextEnabledLowEmphasis)
                    }
                    containerPromoInfoList.addView(promoInfoView)
                }
                if (containerPromoInfoList.childCount > 0) {
                    containerPromoInfoList.show()
                } else {
                    containerPromoInfoList.gone()
                }
            } else {
                containerPromoInfoList.gone()
            }
        }
    }

    private fun renderTimeValidity(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                   element: PromoListItemUiModel) {
        with(viewBinding) {
            val timeValidityInfo = element.uiData.promoInfos.firstOrNull {
                it.type == PromoInfo.TYPE_PROMO_VALIDITY
            }

            if (timeValidityInfo != null) {
                imageTimeValidity.setImageUrl(timeValidityInfo.icon)
                textTimeValidity.text = HtmlLinkHelper(itemView.context, timeValidityInfo.title).spannedString
                buttonPromoDetail.setOnClickListener {
                    listener.onClickPromoItemDetail(element)
                }
                containerTimeValidity.show()
            } else {
                containerTimeValidity.gone()
            }
        }
    }

    private fun renderUserValidity(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                   element: PromoListItemUiModel) {
        with(viewBinding) {
            val bottomBanner = element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_BOTTOM_BANNER }
            if (bottomBanner != null) {
                imageUserValidity.setImageUrl(bottomBanner.icon)
                textUserValidity.text = HtmlLinkHelper(itemView.context, bottomBanner.title).spannedString
                containerUserValidity.show()
            } else {
                containerUserValidity.gone()
            }
        }
    }

    private fun renderClashInfo(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
                                element: PromoListItemUiModel) {
        with(viewBinding) {
            if (element.uiData.clashingInfoMessage.isNotBlank()) {
                imageClashInfo.setImageUrl(element.uiData.clashingInfoIcon)
                textClashInfo.text = HtmlLinkHelper(itemView.context, element.uiData.clashingInfoMessage).spannedString
                containerClashInfo.show()
            } else {
                containerClashInfo.gone()
            }
        }
    }
}