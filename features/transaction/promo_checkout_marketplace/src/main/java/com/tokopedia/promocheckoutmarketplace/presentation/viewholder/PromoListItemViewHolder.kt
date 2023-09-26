package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoCardBinding
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleSubLayoutPromoInfoBinding
import com.tokopedia.promocheckoutmarketplace.presentation.IconHelper
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.LoaderUnify

class PromoListItemViewHolder(
    private val viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
    private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoListItemUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_card

        const val STATE_LOADING = 0
        const val STATE_SELECTED = 1
        const val STATE_ENABLED = 2
        const val STATE_DISABLED = 3

        private const val HEX_COLOR_ALPHA_SUBSTRING = 2
    }

    private val colorTextEnabledDefault = ContextCompat.getColor(
        itemView.context,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
    )
    private val colorTextEnabledLowEmphasis = ContextCompat.getColor(
        itemView.context,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
    )
    private val colorTextDisabled = ContextCompat.getColor(
        itemView.context,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
    )
    private var colorBackgroundSelected =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
    private var colorBackgroundEnabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Background)
    private var colorBackgroundDisabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
    private var colorIconLightSelected =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    private var colorIconDarkSelected =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
    private var colorIconLightEnabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
    private var colorIconDarkEnabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
    private var colorIconLightDisabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
    private var colorIconDarkDisabled =
        ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)

    // variable to identify view holder
    private var currentItemId: String? = null

    override fun bind(element: PromoListItemUiModel) {
        if (element.uiState.isUpdateSelectionStateAction) {
            element.uiState.isUpdateSelectionStateAction = false
            if (currentItemId != null && currentItemId == element.id) {
                renderPromoState(viewBinding, element)
            } else {
                renderPromoData(viewBinding, element)
                renderPromoState(viewBinding, element)
                setPromoItemClickListener(viewBinding, element)
            }
        } else {
            renderPromoData(viewBinding, element)
            renderPromoState(viewBinding, element)
            setPromoItemClickListener(viewBinding, element)
            adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let {
                listener.onShowPromoItem(element, it, getState(element))
            }
        }
    }

    private fun setPromoItemClickListener(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        viewBinding.promoConstraintWrapper.setOnClickListener {
            adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let {
                if (element.uiState.isParentEnabled && !element.uiData.hasClashingPromo &&
                    !element.uiState.isDisabled && !element.uiState.isLoading
                ) {
                    listener.onClickPromoListItem(element, adapterPosition)
                }
            }
        }
    }

    private fun getState(element: PromoListItemUiModel): Int {
        return if (element.uiState.isLoading && !element.uiState.isSelected) {
            STATE_LOADING
        } else {
            if (element.uiState.isParentEnabled && !element.uiData.hasClashingPromo) {
                if (element.uiState.isDisabled) {
                    STATE_DISABLED
                } else {
                    if (element.uiState.isSelected) {
                        STATE_SELECTED
                    } else {
                        STATE_ENABLED
                    }
                }
            } else {
                STATE_DISABLED
            }
        }
    }

    private fun renderPromoState(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        when {
            getState(element) == STATE_LOADING -> {
                renderPromoLoading(viewBinding)
            }
            getState(element) == STATE_SELECTED -> {
                renderPromoSelected(viewBinding, element)
            }
            getState(element) == STATE_DISABLED -> {
                renderPromoDisabled(viewBinding, element)
            }
            else -> {
                renderPromoEnabled(viewBinding, element)
            }
        }
    }

    private fun renderPromoLoading(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding
    ) {
        with(viewBinding) {
            containerPromoLoading.show()
            containerConstraintPromoCheckout.gone()
            promoLoader1.type = LoaderUnify.TYPE_LINE
            promoLoader2.type = LoaderUnify.TYPE_LINE
            cardPromoItem.cardType = CardUnify.TYPE_BORDER
            cardPromoItem.setCardBackgroundColor(colorBackgroundEnabled)
            promoQuantityIdentifierTop.gone()
            promoQuantityIdentifierBottom.gone()
            textPromoQuantity.gone()
            promoConstraintWrapper.setOnClickListener(null)
        }
    }

    private fun renderPromoSelected(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        renderPromoEnabled(viewBinding, element)
        with(viewBinding) {
            containerConstraintPromoCheckout.show()
            containerPromoLoading.gone()
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_selected)
            containerUserValidity.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN100
                )
            )
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_ACTIVE
            cardPromoItem.setCardBackgroundColor(colorBackgroundSelected)

            imageSelectPromo.show()
            updateImaginaryBorderViewVisibility(viewBinding)
            val imageSelectPromoLayoutParam =
                imageSelectPromo.layoutParams as ViewGroup.MarginLayoutParams
            if (element.uiData.remainingPromoCount > 1) {
                if (getPromoInformationDetailsCount(element) <= 1) {
                    imageSelectPromoLayoutParam.topMargin =
                        itemView.context.resources.getDimension(R.dimen.dp_22).toInt()
                } else {
                    imageSelectPromoLayoutParam.topMargin =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)
                            .toInt()
                }
            } else {
                imageSelectPromoLayoutParam.topMargin = 0
            }

            // Need to re render here to achieve dynamic color on selection state
            renderUserValidityState(viewBinding, element)
        }
    }

    private fun renderPromoEnabled(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            containerConstraintPromoCheckout.show()
            containerPromoLoading.gone()
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_enabled)
            promoQuantityIdentifierTop.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_top_enabled)
            promoQuantityIdentifierBottom.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_bottom_enabled)
            textPromoQuantity.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
            )
            textPromoItemTitle.setTextColor(colorTextEnabledDefault)
            textPromoItemTitleInfo.setTextColor(colorTextEnabledLowEmphasis)
            textPromoCodeInfo.setTextColor(colorTextEnabledDefault)
            textPromoCode.setTextColor(colorTextEnabledDefault)
            textTimeValidity.setTextColor(colorTextEnabledLowEmphasis)
            containerUserValidity.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN50
                )
            )
            cardPromoItem.cardType = CardUnify.TYPE_BORDER
            cardPromoItem.setCardBackgroundColor(colorBackgroundEnabled)
            imageSelectPromo.gone()
            updateImaginaryBorderViewVisibility(viewBinding)
            renderUserValidityState(viewBinding, element)
        }
    }

    private fun renderPromoDisabled(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            containerConstraintPromoCheckout.show()
            containerPromoLoading.gone()
            promoHighlightIdentifier.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_highlighted_identifier_disabled)
            promoQuantityIdentifierTop.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_top_disabled)
            promoQuantityIdentifierBottom.setImageResource(R.drawable.promo_checkout_marketplace_module_ic_quantity_identifier_bottom_disabled)
            textPromoQuantity.setTextColor(colorTextDisabled)
            textPromoItemTitle.setTextColor(colorTextDisabled)
            textPromoItemTitleInfo.setTextColor(colorTextDisabled)
            textPromoCodeInfo.setTextColor(colorTextDisabled)
            textPromoCode.setTextColor(colorTextDisabled)
            textTimeValidity.setTextColor(colorTextDisabled)
            containerUserValidity.setBackgroundColor(colorBackgroundDisabled)
            cardPromoItem.cardType = CardUnify.TYPE_BORDER_DISABLED
            cardPromoItem.setCardBackgroundColor(colorBackgroundDisabled)
            imageSelectPromo.gone()
            updateImaginaryBorderViewVisibility(viewBinding)
            renderUserValidityState(viewBinding, element)
        }
    }

    private fun renderIcon(
        iconUnify: IconUnify,
        iconId: Int,
        state: Int,
        isOverrideColorOnSelected: Boolean = false
    ) {
        when (state) {
            STATE_SELECTED -> {
                if (isOverrideColorOnSelected) {
                    iconUnify.setImage(
                        newIconId = iconId,
                        newLightEnable = colorIconLightSelected,
                        newDarkEnable = colorIconDarkSelected
                    )
                } else {
                    iconUnify.setImage(
                        newIconId = iconId,
                        newLightEnable = colorIconLightEnabled,
                        newDarkEnable = colorIconDarkEnabled
                    )
                }
            }
            STATE_ENABLED -> {
                iconUnify.setImage(
                    newIconId = iconId,
                    newLightEnable = colorIconLightEnabled,
                    newDarkEnable = colorIconDarkEnabled
                )
            }
            STATE_DISABLED -> {
                iconUnify.setImage(
                    newIconId = iconId,
                    newLightEnable = colorIconLightDisabled,
                    newDarkEnable = colorIconDarkDisabled
                )
            }
        }
    }

    private fun renderCustomIcon(
        iconUnify: IconUnify,
        customIconResId: Int,
        state: Int,
        isOverrideColorOnSelected: Boolean = false
    ) {
        val iconColor =
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) { // night mode
                when (state) {
                    STATE_SELECTED -> {
                        if (isOverrideColorOnSelected) {
                            colorIconDarkSelected
                        } else {
                            colorIconDarkEnabled
                        }
                    }
                    STATE_ENABLED -> {
                        colorIconDarkEnabled
                    }
                    STATE_DISABLED -> {
                        colorIconDarkDisabled
                    }
                    else -> {
                        colorIconDarkEnabled
                    }
                }
            } else { // light mode
                when (state) {
                    STATE_SELECTED -> {
                        if (isOverrideColorOnSelected) {
                            colorIconLightSelected
                        } else {
                            colorIconLightEnabled
                        }
                    }
                    STATE_ENABLED -> {
                        colorIconLightEnabled
                    }
                    STATE_DISABLED -> {
                        colorIconLightDisabled
                    }
                    else -> {
                        colorIconLightEnabled
                    }
                }
            }
        val iconImg = AppCompatResources.getDrawable(iconUnify.context, customIconResId)
        iconImg?.mutate()
        iconImg?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            iconColor,
            BlendModeCompat.SRC_ATOP
        )
        iconUnify.setImageDrawable(iconImg)
    }

    private fun renderPromoData(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        renderHighlightIdentifier(viewBinding, element)
        renderQuantityIdentifier(viewBinding, element)
        renderTitle(viewBinding, element)
        renderBenefit(viewBinding, element)
        renderPromoCodeIdentifier(viewBinding, element)
        renderPromoInfo(viewBinding, element)
        renderTimeValidity(viewBinding, element)
        renderUserValidity(viewBinding, element)
        renderErrorInfo(viewBinding, element)
        renderDivider(viewBinding, element)
        renderPromoActionable(viewBinding, element)
        adjustConstraints(viewBinding)
        currentItemId = element.id
    }

    private fun renderHighlightIdentifier(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val isHighlighted = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().isHighlighted
            } else {
                element.uiState.isHighlighted
            }
            if (isHighlighted) {
                val topBanner = if (element.uiData.useSecondaryPromo) {
                    element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                } else {
                    element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                }
                if (topBanner != null) {
                    textPromoHighlightIdentifier.text = topBanner.title
                    textPromoHighlightIdentifier.show()
                    promoHighlightIdentifier.show()

                    textPromoHighlightIdentifier.measure(0, 0)
                    val padding =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_14)
                            .toInt()
                    val textPromoHighlightIdentifierWidth =
                        textPromoHighlightIdentifier.measuredWidth
                    promoHighlightIdentifier.layoutParams?.width =
                        2 * padding + textPromoHighlightIdentifierWidth
                    promoHighlightIdentifier.requestLayout()
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

    private fun renderQuantityIdentifier(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            if (element.uiData.remainingPromoCount > 1) {
                textPromoQuantity.text = "Sisa ${element.uiData.remainingPromoCount}"
                textPromoQuantity.show()
                updateImaginaryBorderViewVisibility(viewBinding)
                promoQuantityIdentifierTop.show()
                promoQuantityIdentifierBottom.show()

                val promoQuantityIdentifierLayoutParam =
                    promoQuantityIdentifierTop.layoutParams as ViewGroup.MarginLayoutParams
                val isHighlighted = if (element.uiData.useSecondaryPromo) {
                    element.uiData.secondaryCoupons.first().isHighlighted
                } else {
                    element.uiState.isHighlighted
                }
                val topBanner = if (element.uiData.useSecondaryPromo) {
                    element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                } else {
                    element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
                }
                if (isHighlighted && topBanner != null) {
                    promoQuantityIdentifierLayoutParam.topMargin =
                        itemView.context.resources.getDimension(R.dimen.dp_32).toInt()
                } else {
                    promoQuantityIdentifierLayoutParam.topMargin =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_14)
                            .toInt()
                }
            } else {
                textPromoQuantity.gone()
                updateImaginaryBorderViewVisibility(viewBinding)
                promoQuantityIdentifierTop.gone()
                promoQuantityIdentifierBottom.gone()
            }
        }
    }

    private fun renderTitle(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val title = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().title
            } else {
                element.uiData.title
            }
            textPromoItemTitle.text = title
            val textPromoItemTitleLayoutParam =
                textPromoItemTitle.layoutParams as ViewGroup.MarginLayoutParams
            val isHighlighted = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().isHighlighted
            } else {
                element.uiState.isHighlighted
            }
            val topBanner = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            } else {
                element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            }
            if (isHighlighted && topBanner != null) {
                textPromoItemTitleLayoutParam.topMargin = 0
            } else {
                textPromoItemTitleLayoutParam.topMargin =
                    itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)
                        .toInt()
            }
        }
    }

    private fun renderBenefit(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val textPromoItemTitleInfoLayoutParam =
                textPromoItemTitleInfo.layoutParams as ViewGroup.MarginLayoutParams
            val isHighlighted = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().isHighlighted
            } else {
                element.uiState.isHighlighted
            }
            val topBanner = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            } else {
                element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_TOP_BANNER }
            }
            if (isHighlighted && topBanner != null) {
                textPromoItemTitleInfoLayoutParam.topMargin = 0
            } else {
                textPromoItemTitleInfoLayoutParam.topMargin =
                    itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_8)
                        .toInt()
            }

            val currencyDetailStr = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().currencyDetailStr
            } else {
                element.uiData.currencyDetailStr
            }
            if (currencyDetailStr.isNotBlank()) {
                textPromoItemTitleInfo.text = currencyDetailStr
                textPromoItemTitleInfo.show()
            } else {
                textPromoItemTitleInfo.gone()
            }
        }
    }

    private fun renderPromoCodeIdentifier(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val isAttempted = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().isAttempted
            } else {
                element.uiState.isAttempted
            }
            val promoCode = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().code
            } else {
                element.uiData.promoCode
            }
            if (isAttempted) {
                textPromoCode.text = promoCode
                textPromoCode.show()
                textPromoCodeInfo.show()
            } else {
                textPromoCode.gone()
                textPromoCodeInfo.gone()
            }
        }
    }

    private fun renderPromoInfo(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val promoInfoList = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.filter { it.type == PromoInfo.TYPE_PROMO_INFO && it.title.isNotEmpty() }
            } else {
                element.uiData.promoInfos.filter { it.type == PromoInfo.TYPE_PROMO_INFO && it.title.isNotEmpty() }
            }
            if (promoInfoList.isNotEmpty()) {
                containerPromoInfoList.removeAllViews()
                promoInfoList.forEach {
                    val promoInfoView =
                        PromoCheckoutMarketplaceModuleSubLayoutPromoInfoBinding.inflate(
                            LayoutInflater.from(itemView.context)
                        )
                    if (IconHelper.shouldShowIcon(it.icon)) {
                        if (IconHelper.isCustomIcon(it.icon)) {
                            renderCustomIcon(
                                promoInfoView.iconPromoInfo,
                                IconHelper.getIcon(it.icon),
                                getState(element)
                            )
                        } else {
                            renderIcon(
                                promoInfoView.iconPromoInfo,
                                IconHelper.getIcon(it.icon),
                                getState(element)
                            )
                        }
                    } else {
                        promoInfoView.iconPromoInfo.gone()
                    }
                    promoInfoView.textPromoInfo.text =
                        HtmlLinkHelper(itemView.context, it.title).spannedString
                    if (!element.uiState.isParentEnabled || element.uiState.isDisabled || element.uiData.errorMessage.isNotBlank()) {
                        promoInfoView.textPromoInfo.setTextColor(colorTextDisabled)
                    } else {
                        promoInfoView.textPromoInfo.setTextColor(colorTextEnabledLowEmphasis)
                    }
                    containerPromoInfoList.addView(promoInfoView.root)
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

    private fun renderTimeValidity(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val timeValidityInfo = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_PROMO_VALIDITY }
            } else {
                element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_PROMO_VALIDITY }
            }
            if (timeValidityInfo != null) {
                if (timeValidityInfo.title.isNotBlank() && timeValidityInfo.icon.isNotBlank()) {
                    renderIcon(
                        iconTimeValidity,
                        IconHelper.getIcon(timeValidityInfo.icon),
                        getState(element)
                    )
                    iconTimeValidity.show()
                } else {
                    iconTimeValidity.gone()
                }
                if (timeValidityInfo.title.isNotBlank()) {
                    textTimeValidity.text =
                        HtmlLinkHelper(itemView.context, timeValidityInfo.title).spannedString
                    textTimeValidity.show()
                } else {
                    textTimeValidity.gone()
                }
            } else {
                iconTimeValidity.gone()
                textTimeValidity.gone()
            }

            renderPromoDetailButton(viewBinding, element, timeValidityInfo)
        }
    }

    private fun renderPromoDetailButton(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel,
        timeValidityInfo: PromoInfo?
    ) {
        with(viewBinding) {
            val isAttempted = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().isAttempted
            } else {
                element.uiState.isAttempted
            }
            if (!element.uiState.isBebasOngkir && !isAttempted) {
                buttonPromoDetail.setOnClickListener {
                    listener.onClickPromoItemDetail(element)
                }
                val buttonPromoDetailLayoutParam =
                    buttonPromoDetail.layoutParams as ViewGroup.MarginLayoutParams
                if (timeValidityInfo?.title?.isNotBlank() == true || iconTimeValidity.visibility == View.VISIBLE) {
                    buttonPromoDetailLayoutParam.leftMargin =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_4)
                            .toInt()
                } else {
                    buttonPromoDetailLayoutParam.leftMargin =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_12)
                            .toInt()
                }
                buttonPromoDetail.show()
            } else {
                buttonPromoDetail.gone()
            }
        }
    }

    private fun renderUserValidity(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val bottomBanner = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_BOTTOM_BANNER }
            } else {
                element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_BOTTOM_BANNER }
            }
            if (bottomBanner != null) {
                textUserValidity.text =
                    HtmlLinkHelper(itemView.context, bottomBanner.title).spannedString

                val containerUserValidityLayoutParam =
                    containerUserValidity.layoutParams as ViewGroup.MarginLayoutParams
                val isClashing = if (element.uiData.useSecondaryPromo) {
                    element.uiData.currentClashingPromo.isNotEmpty()
                } else {
                    element.uiData.currentClashingSecondaryPromo.isNotEmpty()
                }
                if (element.uiState.isDisabled || isClashing) {
                    dividerUserValidity.show()
                    containerUserValidityLayoutParam.topMargin = 0
                } else {
                    dividerUserValidity.gone()
                    containerUserValidityLayoutParam.topMargin =
                        itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_4)
                            .toInt()
                }
                containerUserValidity.show()
            } else {
                containerUserValidity.gone()
                dividerUserValidity.gone()
            }
        }
    }

    private fun renderUserValidityState(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            val bottomBanner = if (element.uiData.useSecondaryPromo) {
                element.uiData.secondaryCoupons.first().promoInfos.firstOrNull { it.type == PromoInfo.TYPE_BOTTOM_BANNER }
            } else {
                element.uiData.promoInfos.firstOrNull { it.type == PromoInfo.TYPE_BOTTOM_BANNER }
            }
            if (bottomBanner != null) {
                renderIcon(
                    iconUserValidity,
                    IconHelper.getIcon(bottomBanner.icon),
                    getState(element),
                    true
                )
                when {
                    getState(element) == STATE_SELECTED -> {
                        textUserValidity.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_GN500
                            )
                        )
                    }
                    getState(element) == STATE_DISABLED -> {
                        textUserValidity.setTextColor(colorTextDisabled)
                    }
                    else -> {
                        textUserValidity.setTextColor(colorTextEnabledLowEmphasis)
                    }
                }
            }
        }
    }

    private fun renderErrorInfo(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            if (element.uiData.errorMessage.isNotBlank()) {
                renderIcon(iconErrorInfo, IconUnify.INFORMATION, STATE_ENABLED)
                textErrorInfo.text =
                    HtmlLinkHelper(itemView.context, element.uiData.errorMessage).spannedString
                containerErrorInfo.show()
            } else {
                containerErrorInfo.gone()
            }
        }
    }

    private fun renderDivider(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            if (element.uiState.isParentEnabled && element.uiState.isLastPromoItem) {
                lastPromoDivider.show()
                lastPromoDivider2.invisible()
            } else {
                lastPromoDivider.gone()
                lastPromoDivider2.gone()
            }
        }
    }

    private fun renderPromoActionable(
        viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding,
        element: PromoListItemUiModel
    ) {
        with(viewBinding) {
            cardPromoActionable.shouldShowWithAction(element.uiState.isContainActionableGopayCicilCTA) {
                textPromoActionable.text = MethodChecker.fromHtml(
                    convertToHtmlUnifyColor(element.uiData.cta.text)
                )
                adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let {
                    listener.onShowPromoActionable(element, it)
                }
            }
        }
    }

    private fun convertToHtmlUnifyColor(htmlText: String): String {
        val color = "#" + Integer.toHexString(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            )
        ).substring(HEX_COLOR_ALPHA_SUBSTRING)
        return htmlText
            .replace("<a>", "<font color=$color>")
            .replace("</a>", "</font>")
    }

    private fun getPromoInformationDetailsCount(element: PromoListItemUiModel): Int {
        var promoInformationDetailsCount = 0
        val promoInfos = if (element.uiData.useSecondaryPromo) {
            element.uiData.secondaryCoupons.first().promoInfos
        } else {
            element.uiData.promoInfos
        }
        promoInfos.forEach {
            if (it.type == PromoInfo.TYPE_PROMO_INFO || it.type == PromoInfo.TYPE_BOTTOM_BANNER || it.type == PromoInfo.TYPE_PROMO_VALIDITY) {
                promoInformationDetailsCount++
            }
        }

        return promoInformationDetailsCount
    }

    private fun updateImaginaryBorderViewVisibility(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding) {
        with(viewBinding) {
            if (imageSelectPromo.visibility == View.VISIBLE) {
                imaginaryView.show()
            } else {
                imaginaryView.gone()
            }
        }
    }

    private fun adjustConstraints(viewBinding: PromoCheckoutMarketplaceModuleItemPromoCardBinding) {
        with(viewBinding) {
            val titleLayoutParams = containerPromoTitle.layoutParams as ViewGroup.MarginLayoutParams
            val timeLayoutParams = containerTimeValidity.layoutParams as ViewGroup.MarginLayoutParams
            if (!promoHighlightIdentifier.isVisible && !textPromoCodeInfo.isVisible && !containerPromoInfoList.isVisible &&
                !textTimeValidity.isVisible && !buttonPromoDetail.isVisible && !containerUserValidity.isVisible &&
                !containerErrorInfo.isVisible
            ) {
                titleLayoutParams.topMargin = 8.dpToPx(root.context.resources.displayMetrics)
                timeLayoutParams.topMargin = 10.dpToPx(root.context.resources.displayMetrics)
            } else {
                titleLayoutParams.topMargin = 0
                timeLayoutParams.topMargin = 6.dpToPx(root.context.resources.displayMetrics)
            }
            containerPromoTitle.layoutParams = titleLayoutParams
            containerTimeValidity.layoutParams = timeLayoutParams
        }
    }
}
